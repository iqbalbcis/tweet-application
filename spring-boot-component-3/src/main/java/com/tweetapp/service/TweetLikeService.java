package com.tweetapp.service;

import com.tweetapp.model.TweetLike;

public interface TweetLikeService {

    TweetLike saveTweetLike(TweetLike tweetLike);

    TweetLike updateTweetLike(TweetLike tweetLike);

    TweetLike getTweetLike(String username, Long tweetId);

    Integer deleteAllLikeForAPost(Long tweetId);

    Integer countTotalLike(Long tweetId);
}
