package com.sparta.springlv3.dto;

import com.sparta.springlv3.entity.Comment;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CommentResponseDto implements GeneralResponseDto {
    private String comment;
    private LocalDate modifiedAt;

    public CommentResponseDto(Comment comment) {
        this.comment = comment.getContent();
        this.modifiedAt = comment.getModifiedAt().toLocalDate();
    }
}
