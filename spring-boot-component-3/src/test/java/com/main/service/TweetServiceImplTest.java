package com.main.service;

import com.tweetapp.model.Tweet;
import com.tweetapp.repository.TweetRepository;
import com.tweetapp.service.impl.TweetServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // junit 5
public class TweetServiceImplTest {

    @Mock
    private Tweet tweet;

    @Mock
    private TweetRepository tweetRepository;

    @MockBean
    private TweetServiceImpl tweetService;

    @BeforeEach
    public void setup() throws Exception {
        this.tweetService = new TweetServiceImpl(tweetRepository);
    }

    @Test
    public void testPostTweet() throws Exception {

        when(tweetRepository.save(any(Tweet.class))).thenReturn(tweet);
        assertSame(tweet, tweetService.postTweet(tweet));
        verify(tweetRepository, times(1)).save(any(Tweet.class));
    }
}