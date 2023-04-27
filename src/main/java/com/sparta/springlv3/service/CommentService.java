package com.sparta.springlv3.service;

import com.sparta.springlv3.dto.CommentRequestDto;
import com.sparta.springlv3.dto.CommentResponseDto;
import com.sparta.springlv3.dto.GeneralResponseDto;
import com.sparta.springlv3.dto.StatusResponseDto;
import com.sparta.springlv3.entity.Comment;
import com.sparta.springlv3.entity.Memo;
import com.sparta.springlv3.entity.User;
import com.sparta.springlv3.entity.UserRoleEnum;
import com.sparta.springlv3.repository.CommentRepository;
import com.sparta.springlv3.repository.MemoRepository;
import com.sparta.springlv3.repository.UserRepository;
import com.sparta.springlv3.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final MemoRepository memoRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public GeneralResponseDto create(Long memoId, CommentRequestDto requestDto, HttpServletRequest request) {

        try {
            Claims claims = checkToken(request);
            Memo memo = memoRepository.findById(memoId).orElseThrow(
                    () -> new NullPointerException("존재하지 않는 게시글입니다.")
            );
            User user = userRepository.findById(claims.getSubject()).orElseThrow(
                    () -> new NullPointerException("등록되지 않은 회원입니다.")
            );

            Comment comment = new Comment(requestDto);
            comment.setMemo(memo);
            comment.setUser(user);

            commentRepository.save(comment);
            return new CommentResponseDto(comment);
        } catch (NullPointerException e) {
            return new StatusResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public GeneralResponseDto update(Long commentId, CommentRequestDto requestDto, HttpServletRequest request) {

        try {
            Claims claims = checkToken(request);
            Comment comment = commentRepository.findById(commentId).orElseThrow(
                    () -> new NullPointerException("존재하지 않는 댓글입니다.")
            );

            if (comment.getUser().getUsername().equals(claims.getSubject()) || ((String)claims.get("auth")).equals(UserRoleEnum.ADMIN.getRole())) {
                comment.update(requestDto);
                return new CommentResponseDto(comment);
            }

            return new StatusResponseDto("작성자만 삭제/수정할 수 있습니다.", HttpStatus.BAD_REQUEST);
        } catch (NullPointerException e) {
            return new StatusResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public StatusResponseDto delete(Long commentId, HttpServletRequest request) {

        try {
            Claims claims = checkToken(request);
            Comment comment = commentRepository.findById(commentId).orElseThrow(
                    () -> new NullPointerException("존재하지 않는 댓글입니다.")
            );

            if (comment.getUser().getUsername().equals(claims.getSubject()) || ((String)claims.get("auth")).equals(UserRoleEnum.ADMIN.getRole())) {
                commentRepository.delete(comment);
                return new StatusResponseDto("삭제 성공", HttpStatus.OK);
            }

            return new StatusResponseDto("작성자만 삭제/수정할 수 있습니다.", HttpStatus.BAD_REQUEST);
        } catch (NullPointerException e) {
            return new StatusResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public Claims checkToken(HttpServletRequest request) throws NullPointerException {
        Claims claims = jwtUtil.getUserInfoFromToken(jwtUtil.resolveToken(request));
        if (claims == null) {
            throw new NullPointerException("토큰이 유효하지 않습니다.");
        }
        return claims;
    }
}