package com.tweetapp.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tweet")
@Data
public class Tweet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tweetId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String text;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createDateTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDateTime;

    @Column(nullable = false, length = 100)
    private String username;

    @Transient
    private List<Reply> replyList;

    @Transient
    private Integer totalLikeCount;
}
