package com.sparta.springlv3.controller;

import com.sparta.springlv3.dto.GeneralResponseDto;
import com.sparta.springlv3.dto.MemoRequestDto;
import com.sparta.springlv3.dto.MemoResponseDto;
import com.sparta.springlv3.dto.StatusResponseDto;
import com.sparta.springlv3.service.MemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/memo")
public class MemoController {

    private final MemoService memoService;
    // 게시글 작성하기
    @PostMapping("/create")
    public GeneralResponseDto createMemo(@RequestBody MemoRequestDto requestDto, HttpServletRequest request) {
        return memoService.createMemo(requestDto, request);
    }

    // 전체 게시글 조회하기
    @GetMapping("/list")
    public List<MemoResponseDto> getAllMemo() {
        return memoService.getAllMemo();
    }

    // 특정 게시글 조회하기
    @GetMapping("/{id}")
    public GeneralResponseDto getMemo(@PathVariable Long id) {
        return memoService.getMemo(id);
    }

    // 게시글 수정하기
    @PutMapping("/{id}")
    public GeneralResponseDto updateMemo(@PathVariable Long id, @RequestBody MemoRequestDto requestDto, HttpServletRequest request) {
        return memoService.updateMemo(id, requestDto, request);
    }

    // 게시글 삭제하기
    @DeleteMapping("/{id}")
    public StatusResponseDto deleteMemo(@PathVariable Long id, HttpServletRequest request) {
        return memoService.deleteMemo(id, request);
    }
}
