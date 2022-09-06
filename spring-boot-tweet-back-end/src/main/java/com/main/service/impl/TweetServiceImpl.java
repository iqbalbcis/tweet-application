package com.main.service.impl;

import com.main.model.Tweet;
import com.main.repository.TweetRepository;
import com.main.service.TweetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TweetServiceImpl implements TweetService {

    private TweetRepository tweetRepository;

    @Autowired
    public TweetServiceImpl(final TweetRepository tweetRepository) {
        this.tweetRepository = tweetRepository;
    }

    @Override
    @Transactional
    public Tweet postTweet(Tweet tweet) {
        return tweetRepository.save(tweet);
    }

    @Override
    @Transactional
    public Tweet updateTweet(Tweet tweet) {
        return tweetRepository.save(tweet);
    }

    @Override
    @Transactional
    public void deleteTweet(Long tweetId) {
        tweetRepository.deleteById(tweetId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Tweet> findSpecificUserTweets(String username) {
        return tweetRepository.findSpecificUserTweets(username);
    }

    @Override
    @Transactional(readOnly = true)
    public Tweet findTweeByUsernameAndTweetId(String username, Long tweetId) {
        return tweetRepository.findTweeByUsernameAndTweetId(username, tweetId);
    }

    @Override
    @Transactional(readOnly = true)
    public Tweet findTweeByTweetId(Long tweetId) {
        return tweetRepository.findById(tweetId).orElseThrow(NoSuchElementException::new);
    }

}