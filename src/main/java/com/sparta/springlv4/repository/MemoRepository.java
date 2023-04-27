package com.sparta.springlv4.repository;

import com.sparta.springlv4.entity.Memo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemoRepository extends JpaRepository<Memo, Long> {
}
