package com.tweetapp.repository;

import com.tweetapp.model.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {

    @Query("SELECT r FROM Reply r WHERE r.username = ?1 AND r.tweetId = ?2")
    List<Reply> getAllReplyForSpecificTweetAndUser(String username, Long tweetId);

    @Query("SELECT r FROM Reply r WHERE r.tweetId = ?1")
    List<Reply> getAllReplyForSpecificTweet(Long tweetId);

    @Modifying // can be only void or Integer return type
    @Query("DELETE FROM Reply r WHERE r.tweetId = ?1")
    Integer deleteAllReplyForAPost(Long tweetId); // deleteById - using primary key
}
