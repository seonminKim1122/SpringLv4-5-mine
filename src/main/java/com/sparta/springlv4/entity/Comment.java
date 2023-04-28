package com.sparta.springlv4.entity;

import com.sparta.springlv4.dto.CommentRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    // 댓글 좋아요 갯수
    private Integer likes = 0;

    @OneToMany(mappedBy = "comment")
    private List<CommentLike> commentLikeList = new ArrayList<>();

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

    public void updateLikes(Integer likes) {
        this.likes = likes;
    }
}
