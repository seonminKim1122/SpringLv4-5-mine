package com.sparta.springlv4.controller;

import com.sparta.springlv4.dto.CommentRequestDto;
import com.sparta.springlv4.dto.GeneralResponseDto;
import com.sparta.springlv4.dto.StatusResponseDto;
import com.sparta.springlv4.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/create/{memoId}")
    public GeneralResponseDto create(@PathVariable Long memoId, @RequestBody CommentRequestDto requestDto, HttpServletRequest request) {
        return commentService.create(memoId, requestDto, request);
    }

    @PutMapping("/{commentId}")
    public GeneralResponseDto update(@PathVariable Long commentId, @RequestBody CommentRequestDto requestDto, HttpServletRequest request) {
        return commentService.update(commentId, requestDto, request);
    }

    @DeleteMapping("/{commentId}")
    public StatusResponseDto delete(@PathVariable Long commentId, HttpServletRequest request) {
        return commentService.delete(commentId, request);
    }
}
