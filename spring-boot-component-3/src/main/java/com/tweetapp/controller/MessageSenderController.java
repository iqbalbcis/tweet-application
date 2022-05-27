package com.tweetapp.controller;

import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1.0/tweets")
public class MessageSenderController {

    private static final Logger LOG = LoggerFactory.getLogger(MessageSenderController.class);

    @Value("${springjms.myQueue}")
    private String queue;

    private JmsTemplate jmsTemplate;

    @Autowired
    public MessageSenderController(final JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @ApiOperation(value = "Send message", nickname = "sendMessage", notes = "Send message",
            tags = {"Message"},
            authorizations = { @Authorization(value="jwtToken") }) // value="jwtToken" picked from swagger config
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Message is successfully sent"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Exception occurs while serving the request")})
    @PostMapping(value = "/send/{message}")
    public void send(
            @ApiParam(value = "message", required = true) @PathVariable(value = "message") String message) {

        try {
            LOG.info("Sending a message");
            jmsTemplate.convertAndSend(queue, message);
        }
        catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }
}
