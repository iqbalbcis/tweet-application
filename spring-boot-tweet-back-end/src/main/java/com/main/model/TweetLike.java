package com.main.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "tweetCount")
@Data
public class TweetLike extends Auditable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tweetLk_id")
    private Long tweetLkId;

    @Column(nullable = false, length = 100)
    private String username;

    @Column(name = "tweet_id", nullable = false)
    private Long tweetId;

    @Column(name = "like_count")
    private Integer likeCount;
}
