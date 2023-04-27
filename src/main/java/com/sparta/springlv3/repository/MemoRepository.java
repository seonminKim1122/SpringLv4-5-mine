package com.sparta.springlv3.repository;

import com.sparta.springlv3.entity.Memo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemoRepository extends JpaRepository<Memo, Long> {
}
