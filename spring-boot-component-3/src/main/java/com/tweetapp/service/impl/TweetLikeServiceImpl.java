package com.tweetapp.service.impl;

import com.tweetapp.model.TweetLike;
import com.tweetapp.repository.TweetLikeRepository;
import com.tweetapp.service.TweetLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TweetLikeServiceImpl implements TweetLikeService {

    private TweetLikeRepository tweetLikeRepository;

    @Autowired
    public TweetLikeServiceImpl(final TweetLikeRepository tweetLikeRepository) {
        this.tweetLikeRepository = tweetLikeRepository;
    }

    @Override
    @Transactional
    public TweetLike saveTweetLike(TweetLike tweetLike) {
        return tweetLikeRepository.save(tweetLike);
    }

    @Override
    @Transactional
    public TweetLike updateTweetLike(TweetLike tweetLike) {
        return tweetLikeRepository.save(tweetLike);
    }

    @Override
    @Transactional(readOnly = true)
    public TweetLike getTweetLike(String username, Long tweetId) {
        return tweetLikeRepository.getTweetLike(username, tweetId);
    }

    @Override
    @Transactional
    public Integer deleteAllLikeForAPost(Long tweetId) {
        return tweetLikeRepository.deleteAllLikeForAPost(tweetId);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer countTotalLike(Long tweetId) {
        return tweetLikeRepository.countTotalLike(tweetId);
    }
}
