package com.tweetapp.repository;

import com.tweetapp.model.TweetLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TweetLikeRepository extends JpaRepository<TweetLike, Long> {

    @Query(value = "SELECT t FROM TweetLike t WHERE t.username =?1 AND t.tweetId = ?2")
    TweetLike getTweetLike(String username, Long tweetId);

    @Modifying // can be only void or Integer return type
    @Query("DELETE FROM TweetLike t WHERE t.tweetId = ?1")
    Integer deleteAllLikeForAPost(Long tweetId); // deleteById - using primary key

    @Query("SELECT COUNT(t.likeCount) FROM TweetLike t WHERE t.tweetId = ?1")
    Integer countTotalLike(Long tweetId);
}
