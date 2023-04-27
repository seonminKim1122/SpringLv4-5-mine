package com.sparta.springlv3.entity;

import com.sparta.springlv3.dto.CommentRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne // memo - comment 연관관계의 주인
    @JoinColumn(name = "memo_id")
    private Memo memo;

    @ManyToOne
    @JoinColumn(name = "username")
    private User user;

    public void setMemo(Memo memo) {
        this.memo = memo;
        memo.getComments().add(this);
    }

    public void setUser(User user) {
        this.user = user;
    }
    public Comment(CommentRequestDto requestDto) {
        this.content = requestDto.getComment();
    }

    public void update(CommentRequestDto requestDto) {
        this.content = requestDto.getComment();
    }
}
