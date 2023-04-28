package com.sparta.springlv4.dto;

import com.sparta.springlv4.entity.Memo;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class MemoResponseDto implements GeneralResponseDto {
    private String title;
    private String name;
    private String content;
    private LocalDate modifiedAt;
    private Integer likes;
    private List<CommentResponseDto> comments;

    public MemoResponseDto(Memo memo) {
        this.title = memo.getTitle();
        this.name = memo.getUser().getUsername();
        this.content = memo.getContent();
        this.modifiedAt = memo.getModifiedAt().toLocalDate();
        this.likes = memo.getLikes();
        this.comments = memo.getComments().stream().sorted((c1, c2) -> c2.getModifiedAt().compareTo(c1.getModifiedAt())).map(CommentResponseDto::new).toList();
    }
}
