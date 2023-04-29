package com.sparta.springlv4.service;

import com.sparta.springlv4.dto.CommentRequestDto;
import com.sparta.springlv4.dto.CommentResponseDto;
import com.sparta.springlv4.dto.GeneralResponseDto;
import com.sparta.springlv4.dto.StatusResponseDto;
import com.sparta.springlv4.entity.*;
import com.sparta.springlv4.exception.CustomException;
import com.sparta.springlv4.exception.ErrorCode;
import com.sparta.springlv4.repository.CommentLikeRepository;
import com.sparta.springlv4.repository.CommentRepository;
import com.sparta.springlv4.repository.MemoRepository;
import com.sparta.springlv4.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final MemoRepository memoRepository;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;

    @Transactional
    public GeneralResponseDto create(Long memoId, CommentRequestDto requestDto, UserDetailsImpl userDetails) {
        Memo memo = memoRepository.findById(memoId).orElseThrow(
                () -> new CustomException(ErrorCode.NONEXISTENT_MEMO)
        );

        Comment comment = new Comment(requestDto);
        comment.setMemo(memo);
        comment.setUser(userDetails.getUser());
        commentRepository.save(comment);

        return new CommentResponseDto(comment);
    }

    @Transactional
    public GeneralResponseDto update(Long commentId, CommentRequestDto requestDto, UserDetailsImpl userDetails) {

        Comment comment = findCommentById(commentId);

        if (comment.getUser().getUsername().equals(userDetails.getUsername()) || userDetails.getUser().getRole() == UserRoleEnum.ADMIN) {
            comment.update(requestDto);
            return new CommentResponseDto(comment);
        } else {
            throw new CustomException(ErrorCode.CANNOT_MODIFY_OR_DELETE);
        }
    }

    @Transactional
    public StatusResponseDto delete(Long commentId, UserDetailsImpl userDetails) {

        Comment comment = findCommentById(commentId);

        if (comment.getUser().getUsername().equals(userDetails.getUsername()) || userDetails.getUser().getRole() == UserRoleEnum.ADMIN) {
            commentRepository.delete(comment);
            return new StatusResponseDto("삭제 성공", HttpStatus.OK);
        } else {
            throw new CustomException(ErrorCode.CANNOT_MODIFY_OR_DELETE);
        }
    }

    @Transactional
    public StatusResponseDto likeComment(Long commentId, UserDetailsImpl userDetails) {
        Comment comment = findCommentById(commentId);
        User user = userDetails.getUser();

        // 내가 이 댓글에 이미 좋아요를 눌렀으면 좋아요 취소
        Optional<CommentLike> found = commentLikeRepository.findCommentLikeByCommentAndUser(comment, user);
        if (found.isPresent()) {
            commentLikeRepository.delete(found.get());
            // delete 해도 바로 comment.getCommentLikeList() 에서 사라지지 않음. 일단 숫자를 바꾸는 방법으로 해보자.(전자가 가능하게 하는 방법은 없을까??)
            comment.updateLikes(comment.getLikes() - 1);
            return new StatusResponseDto("좋아요 취소", HttpStatus.OK);
        }

        CommentLike commentLike = new CommentLike(comment, user);
        commentLikeRepository.save(commentLike);
        return new StatusResponseDto("좋아요", HttpStatus.OK);
    }

    public Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> new CustomException(ErrorCode.NONEXISTENT_COMMENT)
        );
    }
}