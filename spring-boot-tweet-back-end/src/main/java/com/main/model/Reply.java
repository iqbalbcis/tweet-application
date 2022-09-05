package com.main.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "reply")
@Data
public class Reply extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id")
    private Long replyId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String text;

    @Column(nullable = false, length = 100)
    private String username;

    @Column(name = "tweet_id", nullable = false)
    private Long tweetId;
}
