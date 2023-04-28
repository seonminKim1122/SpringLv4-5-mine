package com.sparta.springlv4.repository;

import com.sparta.springlv4.entity.Comment;
import com.sparta.springlv4.entity.CommentLike;
import com.sparta.springlv4.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    Optional<CommentLike> findCommentLikeByCommentAndUser(Comment comment, User user);
}
