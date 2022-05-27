package com.tweetapp.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tweetapp.security.model.User;
import lombok.Data;

import java.util.List;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Data
public class ControllerResponse {
    private String message;
    private String description;
    private User user;
    private List<User> usersList;
    private Tweet tweet;
    private List<Tweet> tweetList;
    private Reply reply;
    private String count;
    private List<Reply> replyList;
    private TweetLike tweetLike;
}
