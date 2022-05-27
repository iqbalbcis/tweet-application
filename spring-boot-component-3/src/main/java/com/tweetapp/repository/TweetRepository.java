package com.tweetapp.repository;

import com.tweetapp.model.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {

    @Query(value = "SELECT * FROM tweet t WHERE t.username = ?1 ORDER " +
            "BY t.tweetId DESC LIMIT 100", nativeQuery = true) // sql query
    List<Tweet> findSpecificUserTweets(String username);

    @Query("SELECT t FROM Tweet t WHERE t.username = ?1 AND t.tweetId = ?2")
    Tweet findTweeByUsernameAndTweetId(String username, Long tweetId);
}
