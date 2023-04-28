package com.sparta.springlv4.service;

import com.sparta.springlv4.dto.CommentRequestDto;
import com.sparta.springlv4.dto.CommentResponseDto;
import com.sparta.springlv4.dto.GeneralResponseDto;
import com.sparta.springlv4.dto.StatusResponseDto;
import com.sparta.springlv4.entity.*;
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
        try {
            Memo memo = memoRepository.findById(memoId).orElseThrow(
                    () -> new NullPointerException("존재하지 않는 게시글입니다.")
            );

            Comment comment = new Comment(requestDto);
            comment.setMemo(memo);
            comment.setUser(userDetails.getUser());
            commentRepository.save(comment);

            return new CommentResponseDto(comment);
        } catch (NullPointerException e) {
            return new StatusResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public GeneralResponseDto update(Long commentId, CommentRequestDto requestDto, UserDetailsImpl userDetails) {
        try {
            Comment comment = commentRepository.findById(commentId).orElseThrow(
                    () -> new NullPointerException("존재하지 않는 댓글입니다.")
            );

            if (comment.getUser().getUsername().equals(userDetails.getUsername()) || userDetails.getUser().getRole() == UserRoleEnum.ADMIN) {
                comment.update(requestDto);
                return new CommentResponseDto(comment);
            }

            return new StatusResponseDto("작성자만 삭제/수정할 수 있습니다.", HttpStatus.BAD_REQUEST);
        } catch (NullPointerException e) {
            return new StatusResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public StatusResponseDto delete(Long commentId, UserDetailsImpl userDetails) {
        try {
            Comment comment = commentRepository.findById(commentId).orElseThrow(
                    () -> new NullPointerException("존재하지 않는 댓글입니다.")
            );

            if (comment.getUser().getUsername().equals(userDetails.getUsername()) || userDetails.getUser().getRole() == UserRoleEnum.ADMIN) {
                commentRepository.delete(comment);
                return new StatusResponseDto("삭제 성공", HttpStatus.OK);
            }

            return new StatusResponseDto("작성자만 삭제/수정할 수 있습니다.", HttpStatus.BAD_REQUEST);
        } catch (NullPointerException e) {
            return new StatusResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public StatusResponseDto likeComment(Long commentId, UserDetailsImpl userDetails) {
        try {
            Comment comment = commentRepository.findById(commentId).orElseThrow(
                    () -> new NullPointerException("존재하지 않는 댓글입니다.")
            );
            User user = userDetails.getUser();

            // 이미 존재하는 commentLike 정보이고 내가 누른 좋아요가 맞으면 삭제
            Optional<CommentLike> found = commentLikeRepository.findCommentLikeByCommentAndUser(comment, user);
            if (found.isPresent() && found.get().getUser().getUsername().equals(user.getUsername())) {
                commentLikeRepository.delete(found.get());
                // delete 해도 바로 comment.getCommentLikeList() 에서 사라지지 않음. 일단 숫자를 바꾸는 방법으로 해보자.(전자가 가능하게 하는 방법은 없을까??)
                comment.updateLikes(comment.getLikes() - 1);
                return new StatusResponseDto("좋아요 취소", HttpStatus.OK);
            }

            CommentLike commentLike = new CommentLike(comment, user);
            commentLikeRepository.save(commentLike);
            return new StatusResponseDto("좋아요", HttpStatus.OK);

        } catch (NullPointerException e) {
            return new StatusResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}