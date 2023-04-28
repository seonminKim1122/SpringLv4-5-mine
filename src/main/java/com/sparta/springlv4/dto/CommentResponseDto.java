package com.sparta.springlv4.dto;

import com.sparta.springlv4.entity.Comment;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CommentResponseDto implements GeneralResponseDto {
    private String comment;
    private LocalDate modifiedAt;
    private Integer likes;

    public CommentResponseDto(Comment comment) {
        this.comment = comment.getContent();
        this.modifiedAt = comment.getModifiedAt().toLocalDate();
        this.likes = comment.getLikes();
    }
}
