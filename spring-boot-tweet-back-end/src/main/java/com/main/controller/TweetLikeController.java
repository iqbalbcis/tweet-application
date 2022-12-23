package com.main.controller;

import com.main.model.TweetLike;
import com.main.service.impl.TweetLikeServiceImpl;
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

@RestController
@RequestMapping(value = "/tweets")
@Tag(name = "Like Count")
@Slf4j
public class TweetLikeController {

    private TweetLikeServiceImpl tweetLikeService;

    @Autowired
    public TweetLikeController(final TweetLikeServiceImpl tweetLikeService) {
        this.tweetLikeService = tweetLikeService;
    }

    @Operation(summary = "Update tweet like")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Data is successfully inserted"),
            @ApiResponse(responseCode = "201", description = "Bad request"),
            @ApiResponse(responseCode = "201", description = "Exception occurs while serving the request")})
    @PutMapping(value = "{username}/like/{tweeId}", produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<TweetLike> updateTweetLike(
            @Parameter(description = "User Name", required = true) @PathVariable(value = "username") String username,
            @Parameter(description = "Tweet Id", required = true) @PathVariable(value = "tweeId") Long tweeId,
            @Parameter(description = "Tweet Object", required = true) @Valid @RequestBody TweetLike tweetLikeDetails) {

        log.info("Calling service to update tweet like");

        TweetLike existTweet = tweetLikeService.getTweetLike(username, tweeId);
        if (existTweet == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else if (existTweet.getLikeCount() == null) {
            existTweet.setLikeCount(1);
        } else {
            existTweet.setLikeCount(null);
        }

        TweetLike tweetLike = tweetLikeService.updateTweetLike(existTweet);
        return ResponseEntity.status(HttpStatus.OK).body(tweetLike);
    }

}
