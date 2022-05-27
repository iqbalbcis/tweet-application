package com.tweetapp.service;

import com.tweetapp.model.Tweet;

import java.util.List;

public interface TweetService {

    Tweet postTweet(Tweet tweet);

    Tweet updateTweet(Tweet tweet);

    void deleteTweet(Long tweetId);

    List<Tweet> findSpecificUserTweets(String username);

    Tweet findTweeByUsernameAndTweetId(String username, Long tweetId);

    Tweet findTweeByTweetId(Long tweetId);

}
