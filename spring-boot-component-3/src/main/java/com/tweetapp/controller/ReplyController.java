package com.tweetapp.controller;

import com.tweetapp.constants.Constants;
import com.tweetapp.model.ControllerResponse;
import com.tweetapp.model.Reply;
import com.tweetapp.model.Tweet;
import com.tweetapp.security.model.User;
import com.tweetapp.service.impl.ReplyServiceImpl;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.ws.rs.Produces;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1.0/tweets")
public class ReplyController {

    private static final Logger LOG = LoggerFactory.getLogger(ReplyController.class);

    private ReplyServiceImpl replyService;

    @Autowired
    public ReplyController(final ReplyServiceImpl replyService) {
        this.replyService = replyService;
    }

    @ApiOperation(value = "Post reply", nickname = "postReplyDetails", notes = "Post reply",
            tags = {"Reply"}, response = ControllerResponse.class,
            authorizations = { @Authorization(value="jwtToken") }) // value="jwtToken" picked from swagger config
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Data is successfully inserted"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Exception occurs while serving the request")})
    @PostMapping(value = "{username}/reply/{tweetId}", produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<ControllerResponse> postReplyDetails(
            @ApiParam(value = "User Name", required = true) @PathVariable(value = "username") String username,
            @ApiParam(value = "Tweet Id", required = true) @PathVariable(value = "tweetId") Long tweetId,
            @ApiParam(value = "Reply Object", required = true) @Valid @RequestBody Reply replyDetails) {

        ControllerResponse response = new ControllerResponse();
        HttpStatus httpStatus = null;
        try {
            LOG.info("Calling service to post new reply");
            replyDetails.setUsername(username);
            replyDetails.setTweetId(tweetId);

            Reply reply = replyService.postReply(replyDetails);

            response.setMessage(Constants.CREATION_MESSAGE);
            response.setReply(reply);
            httpStatus = HttpStatus.CREATED;

        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            response.setMessage(Constants.EXCEPTION_MESSAGE);
            response.setDescription(e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(response, httpStatus);
    }

    @ApiOperation(value = "Find all reply for a user for a specific tweet",
            nickname = "findAllReplyForASpecifciTweetAndUser",
            notes = "Find all reply for a user for a specific tweet",
            tags = { "Reply" }, response = ControllerResponse.class,
            authorizations = { @Authorization(value="jwtToken") }) // value="jwtToken" picked from swagger config
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operation Successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Data not found"),
            @ApiResponse(code = 500, message = "Exception occurs while retrieving the request") })
    @Produces({"application/json"})
    @GetMapping(value = "/reply/{tweetId}/{username}")
    public ResponseEntity<ControllerResponse> findAllReplyForASpecifciTweetAndUser(
            @ApiParam(value = "User Name", required = true) @PathVariable(value = "username") String username,
            @ApiParam(value = "Tweet Id", required = true) @PathVariable(value = "tweetId") Long tweetId)
    {
        ControllerResponse response = new ControllerResponse();
        HttpStatus httpStatus = null;
        try
        {
            LOG.info("Calling service to find all reply for a specific tweet and specific user");

            List<Reply> replyList = replyService.getAllReplyForSpecificTweetAndUser(username, tweetId);

            response.setReplyList(replyList);
            httpStatus =HttpStatus.OK;
        }
        catch(Exception e)
        {
            LOG.error(e.getMessage(), e);
            response.setMessage(Constants.EXCEPTION_MESSAGE);
            response.setDescription(e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(response, httpStatus);
    }
}
