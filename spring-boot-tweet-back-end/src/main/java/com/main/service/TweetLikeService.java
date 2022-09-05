package com.main.service;

import com.main.model.TweetLike;
public interface TweetLikeService {

    TweetLike saveTweetLike(TweetLike tweetLike);

    TweetLike updateTweetLike(TweetLike tweetLike);

    TweetLike getTweetLike(String username, Long tweetId);

    Integer deleteAllLikeForAPost(Long tweetId);

    Integer countTotalLike(Long tweetId);
}
