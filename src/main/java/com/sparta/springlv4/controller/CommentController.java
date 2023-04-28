package com.sparta.springlv4.controller;

import com.sparta.springlv4.dto.CommentRequestDto;
import com.sparta.springlv4.dto.GeneralResponseDto;
import com.sparta.springlv4.dto.StatusResponseDto;
import com.sparta.springlv4.security.UserDetailsImpl;
import com.sparta.springlv4.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/create/{memoId}")
    public GeneralResponseDto create(@PathVariable Long memoId, @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.create(memoId, requestDto, userDetails);
    }

    @PutMapping("/{commentId}")
    public GeneralResponseDto update(@PathVariable Long commentId, @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.update(commentId, requestDto, userDetails);
    }

    @DeleteMapping("/{commentId}")
    public StatusResponseDto delete(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.delete(commentId, userDetails);
    }

    @PostMapping("/like/{commentId}")
    public StatusResponseDto likeComment(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.likeComment(commentId, userDetails);
    }
}
