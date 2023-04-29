package com.sparta.springlv4.service;

import com.sparta.springlv4.dto.GeneralResponseDto;
import com.sparta.springlv4.dto.MemoRequestDto;
import com.sparta.springlv4.dto.MemoResponseDto;
import com.sparta.springlv4.dto.StatusResponseDto;
import com.sparta.springlv4.entity.Memo;
import com.sparta.springlv4.entity.MemoLike;
import com.sparta.springlv4.entity.User;
import com.sparta.springlv4.entity.UserRoleEnum;
import com.sparta.springlv4.exception.CustomException;
import com.sparta.springlv4.exception.ErrorCode;
import com.sparta.springlv4.repository.MemoLikeRepository;
import com.sparta.springlv4.repository.MemoRepository;
import com.sparta.springlv4.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemoService {

    private final MemoRepository memoRepository;
    private final MemoLikeRepository memoLikeRepository;

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
        Memo memo = findMemoById(id);
        return new MemoResponseDto(memo);
    }

    @Transactional
    public GeneralResponseDto updateMemo(Long id, MemoRequestDto requestDto, UserDetailsImpl userDetails) {

        Memo memo = findMemoById(id);

        if (memo.getUser().getUsername().equals(userDetails.getUsername()) || userDetails.getUser().getRole() == UserRoleEnum.ADMIN) {
            memo.update(requestDto);
            return new MemoResponseDto(memo);
        } else {
            throw new CustomException(ErrorCode.CANNOT_MODIFY_OR_DELETE);
        }
    }

    @Transactional
    public StatusResponseDto deleteMemo(Long id, UserDetailsImpl userDetails) {
        Memo memo = findMemoById(id);

        if (memo.getUser().getUsername().equals(userDetails.getUsername()) || userDetails.getUser().getRole() == UserRoleEnum.ADMIN) {
            memoRepository.delete(memo);
            return new StatusResponseDto("삭제 완료", HttpStatus.OK);
        } else {
            throw new CustomException(ErrorCode.CANNOT_MODIFY_OR_DELETE);
        }
    }

    @Transactional
    public StatusResponseDto likeMemo(Long id, UserDetailsImpl userDetails) {

        Memo memo = findMemoById(id);
        User user = userDetails.getUser();
        // 이미 존재하는 memoLike 정보이고 내가 누른 좋아요면 삭제
        Optional<MemoLike> found = memoLikeRepository.findMemoLikeByMemoAndUser(memo, user);
        if(found.isPresent()) {
            memoLikeRepository.delete(found.get());
            memo.updateLikes(memo.getLikes() - 1);
            return new StatusResponseDto("좋아요 취소", HttpStatus.OK);
        }

        MemoLike memoLike = new MemoLike(memo, user);
        memoLikeRepository.save(memoLike);
        return new StatusResponseDto("좋아요", HttpStatus.OK);
    }

    public Memo findMemoById(Long id) throws NullPointerException {
        return memoRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.NONEXISTENT_MEMO)
        );
    }
}
