package com.tweetapp.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class LoginStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String username;

    @Column(length = 10, nullable = false)
    private String loginStatus;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timeStamp;
}
