package com.main.controller;

import com.main.model.Tweet;
import com.main.model.TweetLike;
import com.main.security.model.User;
import com.main.security.service.impl.UserServiceImpl;
import com.main.service.impl.ReplyServiceImpl;
import com.main.service.impl.TweetLikeServiceImpl;
import com.main.service.impl.TweetServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import javax.ws.rs.Produces;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/tweets")
@Tag(name = "Tweet")
@Slf4j
public class TweetController {

    private TweetServiceImpl tweetService;
    private UserServiceImpl userService;
    private TweetLikeServiceImpl tweetLikeService;
    private ReplyServiceImpl replyService;

    @Autowired
    public TweetController(
            final TweetServiceImpl tweetService,
            final UserServiceImpl userService,
            final TweetLikeServiceImpl tweetLikeService,
            final ReplyServiceImpl replyService) {
        this.tweetService = tweetService;
        this.userService = userService;
        this.tweetLikeService = tweetLikeService;
        this.replyService = replyService;
    }

    @Operation(summary = "This operation is used to create tweet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Creation of tweet"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Exception occurred while serving the request")})
    @PostMapping(value = "/add-tweet", produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<Tweet> postTweetDetails(
            @Parameter(description = "Tweet Object", required = true) @Valid @RequestBody Tweet tweetDetails) {

        log.info("Calling service to post new tweet");

        User existUser = userService.findUser(tweetDetails.getUsername());

        tweetDetails.setCreationDate(new Date());
        tweetDetails.setUsername(tweetDetails.getUsername());
        Tweet tweet = tweetService.postTweet(tweetDetails);

        TweetLike tweetLike = new TweetLike();
        tweetLike.setUsername(tweet.getUsername());
        tweetLike.setTweetId(tweet.getTweetId());
        tweetLike.setLikeCount(null);
        tweetLikeService.saveTweetLike(tweetLike);

        return ResponseEntity.status(HttpStatus.CREATED).body(tweet);
    }

    @Operation(summary = "This operation is used to update TweetDetails")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Data is successfully updated"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Exception occurred while serving the request")})
    @PutMapping(value = "/update/{tweeId}", produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<Tweet> updateTweetDetails(
            @Parameter(description = "Tweet Id", required = true) @PathVariable(value = "tweeId") Long tweeId,
            @Parameter(description = "Tweet Object", required = true) @Valid @RequestBody Tweet tweetDetails) {

        log.info("Calling service to update tweet text");
        Tweet existTweet = tweetService.findTweeByTweetId(tweeId);
        existTweet.setText(tweetDetails.getText());
        Tweet tweet = tweetService.postTweet(existTweet);

        return ResponseEntity.status(HttpStatus.OK).body(tweet);
    }

    @Operation(summary = "delete tweet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation Successful"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Data not found"),
            @ApiResponse(responseCode = "500", description = "Exception occurs while retrieving the request")})
    @DeleteMapping(value = "/delete/{tweetId}")
    public ResponseEntity<?> deleteTweetDetails(
            @Parameter(description = "Tweet Id", required = true) @PathVariable(value = "tweetId") Long tweetId) {

        log.info("Calling service to delete tweet");
        tweetService.deleteTweet(tweetId);
        replyService.deleteAllReplyForAPost(tweetId);
        tweetLikeService.deleteAllLikeForAPost(tweetId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "This operation is used to find all tweet for a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation Successful"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Data not found"),
            @ApiResponse(responseCode = "500", description = "Exception occurred while serving the request")})
    @GetMapping(value = "/{username}", produces = {"application/json"})
    public ResponseEntity<List<Tweet>> findAllTweetsForAUser(
            @Parameter(description = "User Name", required = true) @PathVariable(value = "username") String username) {
        log.info("Calling service for find all tweets for a specific user");

        List<Tweet> tweetList = tweetService.findSpecificUserTweets(username);

        List<Tweet> tweetListWithCount = new ArrayList<>();
        if (!tweetList.isEmpty()) {
            for (Tweet tweet : tweetList) {
                tweet.setTotalLikeCount(tweetLikeService.countTotalLike(tweet.getTweetId()));
                tweetListWithCount.add(tweet);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(tweetListWithCount);
    }

    @Operation(summary = "This operation is used to find all Tweets Reply And Like for a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Find all Tweets Reply And Like"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Exception occurred while serving the request")})
    @Produces({"application/json"})
    @GetMapping(value = "/reply/like/{username}")
    public ResponseEntity<List<Tweet>> findAllTweetsAndReplyAndTotalLike(
            @Parameter(description = "User Name", required = true) @PathVariable(value = "username") String username) {
        log.info("Calling service for find all tweets for a specific user");

        List<Tweet> tweetList = tweetService.findSpecificUserTweets(username);

        List<Tweet> tweetListWithCount = new ArrayList<>();
        if (!tweetList.isEmpty()) {
            for (Tweet tweet : tweetList) {
                tweet.setTotalLikeCount(tweetLikeService.countTotalLike(tweet.getTweetId()));
                tweet.setReplyList(replyService.getAllReplyForSpecificTweet(tweet.getTweetId()));
                tweetListWithCount.add(tweet);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(tweetListWithCount);
    }
}
