package com.sparta.springlv4.repository;

import com.sparta.springlv4.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
