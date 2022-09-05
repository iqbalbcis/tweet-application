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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;

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

//    @ApiOperation(value = "Update tweet", nickname = "updateTweetDetails", notes = "Update existing tweet",
//            tags = {"Tweet"}, response = ControllerResponse.class,
//            authorizations = { @Authorization(value="jwtToken") }) // value="jwtToken" picked from swagger config
//    @ApiResponses(value = {
//            @ApiResponse(code = 201, message = "Data is successfully inserted"),
//            @ApiResponse(code = 400, message = "Bad request"),
//            @ApiResponse(code = 500, message = "Exception occurs while serving the request")})
//    @PutMapping(value = "/update/{tweeId}", produces = {"application/json"}, consumes = {"application/json"})
//    public ResponseEntity<ControllerResponse> updateTweetDetails(
//            @ApiParam(value = "Tweet Id", required = true) @PathVariable(value = "tweeId") Long tweeId,
//            @ApiParam(value = "Tweet Object", required = true) @Valid @RequestBody Tweet tweetDetails) {
//
//        ControllerResponse response = new ControllerResponse();
//        HttpStatus httpStatus = null;
//        try {
//            LOG.info("Calling service to update tweet text");
//
//            Tweet existTweet = tweetService.findTweeByTweetId(tweeId);
//            if(existTweet == null)
//            {
//                response.setMessage(Constants.NOTEXIST_MESSAGE);
//                httpStatus = HttpStatus.NOT_FOUND;
//                return new ResponseEntity<>(response, httpStatus);
//            }
//
//            existTweet.setText(tweetDetails.getText());
//            Tweet tweet = tweetService.postTweet(existTweet);
//
//            response.setMessage(Constants.UPPDATE_MESSAGE);
//            response.setTweet(tweet);
//            httpStatus = HttpStatus.OK;
//
//        } catch (Exception e) {
//            LOG.error(e.getMessage(), e);
//            response.setMessage(Constants.EXCEPTION_MESSAGE);
//            response.setDescription(e.getMessage());
//            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
//        }
//        return new ResponseEntity<>(response, httpStatus);
//    }
//
//    @ApiOperation(value = "Delete tweet", nickname = "deleteTweetDetails", notes = "Delete tweet",
//            tags = {"Tweet"}, response = ControllerResponse.class,
//            authorizations = { @Authorization(value="jwtToken") }) // value="jwtToken" picked from swagger config
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Operation Successful"),
//            @ApiResponse(code = 400, message = "Bad request"),
//            @ApiResponse(code = 404, message = "Data not exist"),
//            @ApiResponse(code = 500, message = "Exception occurs while retrieving the request") })
//    @DeleteMapping(value = "/delete/{tweetId}")
//    public ResponseEntity<ControllerResponse> deleteTweetDetails(
//            @ApiParam(value = "Tweet Id", required = true) @PathVariable(value = "tweetId") Long tweetId) {
//
//        ControllerResponse response = new ControllerResponse();
//        HttpStatus httpStatus = null;
//        try {
//            LOG.info("Calling service to delete tweet");
//
//            tweetService.deleteTweet(tweetId);
//            replyService.deleteAllReplyForAPost(tweetId);
//            tweetLikeService.deleteAllLikeForAPost(tweetId);
//
//            response.setMessage(Constants.DELETE_MESSAGE);
//            httpStatus = HttpStatus.OK;
//
//        } catch (Exception e) {
//            LOG.error(e.getMessage(), e);
//            response.setMessage(Constants.EXCEPTION_MESSAGE);
//            response.setDescription(e.getMessage());
//            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
//        }
//        return new ResponseEntity<>(response, httpStatus);
//    }
//
//    @ApiOperation(value = "Find all tweet for a user", nickname = "findAllTweetsForAUser", notes = "Find all tweet for a user",
//            tags = { "Tweet" }, response = ControllerResponse.class,
//            authorizations = { @Authorization(value="jwtToken") }) // value="jwtToken" picked from swagger config
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Operation Successful"),
//            @ApiResponse(code = 400, message = "Bad request"),
//            @ApiResponse(code = 404, message = "Data not found"),
//            @ApiResponse(code = 500, message = "Exception occurs while retrieving the request") })
//    @Produces({"application/json"})
//    @GetMapping(value = "/{username}")
//    public ResponseEntity<ControllerResponse> findAllTweetsForAUser(
//            @ApiParam(value = "User Name", required = true) @PathVariable(value = "username") String username)
//    {
//        ControllerResponse response = new ControllerResponse();
//        HttpStatus httpStatus = null;
//        try
//        {
//            LOG.info("Calling service for find all tweets for a specific user");
//
//            List<Tweet> tweetList = tweetService.findSpecificUserTweets(username);
//
//            List<Tweet> tweetListWithCount = new ArrayList<>();
//            if(!tweetList.isEmpty())
//            {
//                for(Tweet tweet:tweetList) {
//                    tweet.setTotalLikeCount(tweetLikeService.countTotalLike(tweet.getTweetId()));
//                    tweetListWithCount.add(tweet);
//                }
//            }
//
//            response.setTweetList(tweetListWithCount);
//            httpStatus =HttpStatus.OK;
//        }
//        catch(Exception e)
//        {
//            LOG.error(e.getMessage(), e);
//            response.setMessage(Constants.EXCEPTION_MESSAGE);
//            response.setDescription(e.getMessage());
//            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
//        }
//        return new ResponseEntity<>(response, httpStatus);
//    }
//
//    @ApiOperation(value = "Find all for a user", nickname = "findAllTweetsAndReplyAndTotalLike", notes = "Find all for a user",
//            tags = { "Tweet" }, response = ControllerResponse.class,
//            authorizations = { @Authorization(value="jwtToken") }) // value="jwtToken" picked from swagger config
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Operation Successful"),
//            @ApiResponse(code = 400, message = "Bad request"),
//            @ApiResponse(code = 404, message = "Data not found"),
//            @ApiResponse(code = 500, message = "Exception occurs while retrieving the request") })
//    @Produces({"application/json"})
//    @GetMapping(value = "/reply/like/{username}")
//    public ResponseEntity<ControllerResponse> findAllTweetsAndReplyAndTotalLike(
//            @ApiParam(value = "User Name", required = true) @PathVariable(value = "username") String username)
//    {
//        ControllerResponse response = new ControllerResponse();
//        HttpStatus httpStatus = null;
//        try
//        {
//            LOG.info("Calling service for find all tweets for a specific user");
//
//            List<Tweet> tweetList = tweetService.findSpecificUserTweets(username);
//
//            List<Tweet> tweetListWithCount = new ArrayList<>();
//            if(!tweetList.isEmpty())
//            {
//                for(Tweet tweet:tweetList) {
//                    tweet.setTotalLikeCount(tweetLikeService.countTotalLike(tweet.getTweetId()));
//                    tweet.setReplyList(replyService.getAllReplyForSpecificTweet(tweet.getTweetId()));
//                    tweetListWithCount.add(tweet);
//                }
//            }
//
//            response.setTweetList(tweetListWithCount);
//            httpStatus =HttpStatus.OK;
//        }
//        catch(Exception e)
//        {
//            LOG.error(e.getMessage(), e);
//            response.setMessage(Constants.EXCEPTION_MESSAGE);
//            response.setDescription(e.getMessage());
//            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
//        }
//        return new ResponseEntity<>(response, httpStatus);
//    }
//
//    @ApiOperation(value = "generate error", nickname = "generateError", notes = "generate error",
//            tags = { "Tweet" }, authorizations = { @Authorization(value="jwtToken") })
//    @GetMapping(value = "/generateError")
//    public ResponseEntity<?> generateError() {
//        try
//        {
//            throw new RuntimeException("generated error");
//        }
//        catch(Exception e)
//        {
//            LOG.error(e.getMessage(), e);
//        }
//        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//    }
}
