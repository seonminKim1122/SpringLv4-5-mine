package com.sparta.springlv4.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class MemoLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="memo_id")
    private Memo memo;

    @ManyToOne
    @JoinColumn(name="username")
    private User user;

    public MemoLike(Memo memo, User user) {
        this.memo = memo;
        this.user = user;
        memo.getMemoLikeList().add(this);
        memo.updateLikes(memo.getLikes() + 1);
    }
}
