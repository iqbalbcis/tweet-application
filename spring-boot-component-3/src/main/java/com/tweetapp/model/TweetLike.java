package com.tweetapp.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "tweetCount")
@Data
public class TweetLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tweetLkId;

    @Column(nullable = false, length = 100)
    private String username;

    @Column(nullable = false)
    private Long tweetId;

    private Integer likeCount;
}
