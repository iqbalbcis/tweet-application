package com.tweetapp.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "reply")
@Data
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long replyId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String text;

    @Column(nullable = false, length = 100)
    private String username;

    @Column(nullable = false)
    private Long tweetId;
}
