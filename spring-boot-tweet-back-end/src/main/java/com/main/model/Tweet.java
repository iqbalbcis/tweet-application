package com.main.model;

import lombok.Data;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "tweet")
@Data
public class Tweet extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tweet_id")
    private Long tweetId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String text;

    @Column(nullable = false, length = 100)
    private String username;

    @Transient
    private List<Reply> replyList;

    @Transient
    private Integer totalLikeCount;
}
