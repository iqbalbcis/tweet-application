package com.tweetapp.service;

import com.tweetapp.model.Reply;

import java.util.List;

public interface ReplyService {

    Reply postReply(Reply reply);

    List<Reply> getAllReplyForSpecificTweetAndUser(String username, Long tweetId);

    Integer deleteAllReplyForAPost(Long tweetId);

    List<Reply> getAllReplyForSpecificTweet(Long tweetId);
}
