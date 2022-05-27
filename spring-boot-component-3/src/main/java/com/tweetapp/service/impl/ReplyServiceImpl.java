package com.tweetapp.service.impl;

import com.tweetapp.model.Reply;
import com.tweetapp.repository.ReplyRepository;
import com.tweetapp.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReplyServiceImpl implements ReplyService {

    private ReplyRepository replyRepository;

    @Autowired
    public ReplyServiceImpl(final ReplyRepository replyRepository) {
        this.replyRepository = replyRepository;
    }

    @Override
    @Transactional
    public Reply postReply(Reply reply) {
        return replyRepository.save(reply);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reply> getAllReplyForSpecificTweetAndUser(String username, Long tweetId) {
        return replyRepository.getAllReplyForSpecificTweetAndUser(username, tweetId);
    }

    @Override
    @Transactional
    public Integer deleteAllReplyForAPost(Long tweetId) {
        return replyRepository.deleteAllReplyForAPost(tweetId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reply> getAllReplyForSpecificTweet(Long tweetId) {
        return replyRepository.getAllReplyForSpecificTweet(tweetId);
    }
}
