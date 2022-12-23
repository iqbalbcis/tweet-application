package com.main.controller;

import com.main.model.Reply;
import com.main.service.impl.ReplyServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import javax.ws.rs.Produces;
import java.util.List;

@RestController
@RequestMapping(value = "/tweets")
@Tag(name = "Reply")
@Slf4j
public class ReplyController {

    private static final Logger LOG = LoggerFactory.getLogger(ReplyController.class);

    private ReplyServiceImpl replyService;

    @Autowired
    public ReplyController(final ReplyServiceImpl replyService) {
        this.replyService = replyService;
    }

    @Operation(summary = "This operation is used to create tweet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Creation of reply"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Exception occurred while serving the request")})
    @PostMapping(value = "{username}/reply/{tweetId}", produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<Reply> postReplyDetails(
            @Parameter(description = "User Name", required = true) @PathVariable(value = "username") String username,
            @Parameter(description = "Tweet Id", required = true) @PathVariable(value = "tweetId") Long tweetId,
            @Parameter(description = "Reply Object", required = true) @Valid @RequestBody Reply replyDetails) {

        log.info("Calling service to post new reply");
        replyDetails.setUsername(username);
        replyDetails.setTweetId(tweetId);

        Reply reply = replyService.postReply(replyDetails);
        return ResponseEntity.status(HttpStatus.CREATED).body(reply);
    }

    @Operation(summary = "Find all reply for a user for a specific tweet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation Successful"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Data not found"),
            @ApiResponse(responseCode = "500", description = "Exception occurs while retrieving the request")})
    @Produces({"application/json"})
    @GetMapping(value = "/reply/{tweetId}/{username}")
    public ResponseEntity<List<Reply>> findAllReplyForASpecifciTweetAndUser(
            @Parameter(description = "User Name", required = true) @PathVariable(value = "username") String username,
            @Parameter(description = "Tweet Id", required = true) @PathVariable(value = "tweetId") Long tweetId) {

        log.info("Calling service to find all reply for a specific tweet and specific user");

        List<Reply> replyList = replyService.getAllReplyForSpecificTweetAndUser(username, tweetId);
        return ResponseEntity.status(HttpStatus.OK).body(replyList);
    }
}
