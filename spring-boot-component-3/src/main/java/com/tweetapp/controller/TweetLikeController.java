package com.tweetapp.controller;

import com.tweetapp.constants.Constants;
import com.tweetapp.model.ControllerResponse;
import com.tweetapp.model.TweetLike;
import com.tweetapp.service.impl.TweetLikeServiceImpl;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/v1.0/tweets")
public class TweetLikeController {

    private static final Logger LOG = LoggerFactory.getLogger(TweetLikeController.class);

    private TweetLikeServiceImpl tweetLikeService;

    @Autowired
    public TweetLikeController(final TweetLikeServiceImpl tweetLikeService) {
        this.tweetLikeService = tweetLikeService;
    }

    @ApiOperation(value = "Update tweet like", nickname = "updateTweetLike", notes = "Update existing tweet like",
            tags = {"TweetLike"}, response = ControllerResponse.class,
            authorizations = { @Authorization(value="jwtToken") }) // value="jwtToken" picked from swagger config
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Data is successfully inserted"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Exception occurs while serving the request")})
    @PutMapping(value = "{username}/like/{tweeId}", produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<ControllerResponse> updateTweetLike(
            @ApiParam(value = "User Name", required = true) @PathVariable(value = "username") String username,
            @ApiParam(value = "Tweet Id", required = true) @PathVariable(value = "tweeId") Long tweeId,
            @ApiParam(value = "Tweet Object", required = true) @Valid @RequestBody TweetLike tweetLikeDetails) {

        ControllerResponse response = new ControllerResponse();
        HttpStatus httpStatus = null;
        try {
            LOG.info("Calling service to update tweet like");

            TweetLike existTweet = tweetLikeService.getTweetLike(username, tweeId);
            if(existTweet == null)
            {
                response.setMessage(Constants.NOTEXIST_MESSAGE);
                httpStatus = HttpStatus.NOT_FOUND;
                return new ResponseEntity<>(response, httpStatus);
            }
            else if(existTweet.getLikeCount() == null) {
                existTweet.setLikeCount(1);
            }
            else {
                existTweet.setLikeCount(null);
            }

            TweetLike tweetLike = tweetLikeService.updateTweetLike(existTweet);

            response.setMessage(Constants.UPPDATE_MESSAGE);
            response.setTweetLike(tweetLike);
            httpStatus = HttpStatus.OK;

        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            response.setMessage(Constants.EXCEPTION_MESSAGE);
            response.setDescription(e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(response, httpStatus);
    }

}
