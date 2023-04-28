package com.sparta.springlv4.repository;

import com.sparta.springlv4.entity.Memo;
import com.sparta.springlv4.entity.MemoLike;
import com.sparta.springlv4.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemoLikeRepository extends JpaRepository<MemoLike, Long> {
    Optional<MemoLike> findMemoLikeByMemoAndUser(Memo memo, User user);
}
