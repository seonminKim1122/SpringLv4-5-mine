package com.sparta.springlv3.repository;

import com.sparta.springlv3.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
