package com.sparta.springlv4.service;

import com.sparta.springlv4.dto.CommentRequestDto;
import com.sparta.springlv4.dto.CommentResponseDto;
import com.sparta.springlv4.dto.GeneralResponseDto;
import com.sparta.springlv4.dto.StatusResponseDto;
import com.sparta.springlv4.entity.Comment;
import com.sparta.springlv4.entity.Memo;
import com.sparta.springlv4.entity.UserRoleEnum;
import com.sparta.springlv4.repository.CommentRepository;
import com.sparta.springlv4.repository.MemoRepository;
import com.sparta.springlv4.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final MemoRepository memoRepository;
    private final CommentRepository commentRepository;

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
}