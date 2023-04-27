package com.sparta.springlv4.service;

import com.sparta.springlv4.dto.GeneralResponseDto;
import com.sparta.springlv4.dto.MemoRequestDto;
import com.sparta.springlv4.dto.MemoResponseDto;
import com.sparta.springlv4.dto.StatusResponseDto;
import com.sparta.springlv4.entity.Memo;
import com.sparta.springlv4.entity.User;
import com.sparta.springlv4.entity.UserRoleEnum;
import com.sparta.springlv4.repository.MemoRepository;
import com.sparta.springlv4.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemoService {

    private final MemoRepository memoRepository;

    @Transactional
    public GeneralResponseDto createMemo(MemoRequestDto requestDto, UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        Memo memo = new Memo(requestDto);
        memo.setUser(user);
        memoRepository.save(memo);
        return new MemoResponseDto(memo);
    }

    public List<MemoResponseDto> getAllMemo() {
        List<Memo> memoList = memoRepository.findAll();
        return memoList.stream().sorted((memo1, memo2) -> memo2.getModifiedAt().compareTo(memo1.getModifiedAt())).map(MemoResponseDto::new).collect(Collectors.toList());
    }

    public GeneralResponseDto getMemo(Long id) {
        try {
            Memo memo = findMemoById(id);
            return new MemoResponseDto(memo);
        } catch (NullPointerException e){
            return new StatusResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public GeneralResponseDto updateMemo(Long id, MemoRequestDto requestDto, UserDetailsImpl userDetails) {
        try {
            Memo memo = findMemoById(id);

            if (memo.getUser().getUsername().equals(userDetails.getUsername()) || userDetails.getUser().getRole() == UserRoleEnum.ADMIN) {
                memo.update(requestDto);
                return new MemoResponseDto(memo);
            }

            return new StatusResponseDto("직접 작성한 게시글만 수정 가능합니다.", HttpStatus.BAD_REQUEST);
        } catch (NullPointerException e) {
            return new StatusResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST);
        }


    }

    @Transactional
    public StatusResponseDto deleteMemo(Long id, UserDetailsImpl userDetails) {
        try {
            Memo memo = findMemoById(id);

            if (memo.getUser().getUsername().equals(userDetails.getUsername()) || userDetails.getUser().getRole() == UserRoleEnum.ADMIN) {
                memoRepository.delete(memo);
                return new StatusResponseDto("삭제 완료", HttpStatus.OK);
            }

            return new StatusResponseDto("직접 작성한 게시글만 삭제 가능합니다.", HttpStatus.BAD_REQUEST);
        } catch (NullPointerException e) {
            return new StatusResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public Memo findMemoById(Long id) throws NullPointerException {
        return memoRepository.findById(id).orElseThrow(
                () -> new NullPointerException("존재하지 않는 게시글입니다.")
        );
    }
}
