package com.tweetapp.security.controller;

import com.tweetapp.constants.Constants;
import com.tweetapp.global.GlobalForAll;
import com.tweetapp.model.ControllerResponse;
import com.tweetapp.security.model.User;
import com.tweetapp.security.service.impl.UserServiceImpl;
import com.tweetapp.security.util.JwtUtil;
import com.tweetapp.util.EmailUtility;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.Produces;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1.0/tweets")
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    private UserServiceImpl userService;
    private PasswordEncoder passwordEncode;
    private AuthenticationController authenticationController;
    private JwtUtil jwtTokenUtil;
    private GlobalForAll global;

    @Autowired
    public UserController(final UserServiceImpl userService,
                          final PasswordEncoder passwordEncode,
                          final AuthenticationController authenticationController,
                          final GlobalForAll global,
                          final JwtUtil jwtTokenUtil) {
        this.userService = userService;
        this.passwordEncode = passwordEncode;
        this.authenticationController = authenticationController;
        this.global = global;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @ApiOperation(value = "Register User", nickname = "registerUserDetails", notes = "Create user",
            tags = {"User"}, response = ControllerResponse.class,
            authorizations = { @Authorization(value="jwtToken") }) // value="jwtToken" picked from swagger config
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Data is successfully inserted"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Exception occurs while serving the request")})
    @PostMapping(value = "/register", produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<ControllerResponse> registerUserDetails(
            @ApiParam(value = "User Object", required = true) @Valid @RequestBody User userDetails) {

        ControllerResponse response = new ControllerResponse();
        HttpStatus httpStatus = null;
        try {
            LOG.info("Calling service to register user");

            User existUser = userService.findByUsername(userDetails.getUsername().trim());
            if(existUser !=null)
            {
                response.setMessage(Constants.USER_EXIST_MESSAGE);
                httpStatus = HttpStatus.CONFLICT;
                return new ResponseEntity<>(response, httpStatus);
            }
            String plainPass = userDetails.getPassWord();
            userDetails.setPassWord(passwordEncode.encode(userDetails.getPassWord()));
            userDetails.setCreateDateTime(new Date());
            User user = userService.userRegistration(userDetails);

            response.setMessage(Constants.CREATION_MESSAGE);
            response.setUser(user);
            httpStatus = HttpStatus.CREATED;

        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            response.setMessage(Constants.EXCEPTION_MESSAGE);
            response.setDescription(e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(response, httpStatus);
    }

    @ApiOperation(value = "Forget Password", nickname = "forgetPassword", notes = "Forget Password",
            tags = {"User"}, response = ControllerResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Data is successfully inserted"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Exception occurs while serving the request")})
    @GetMapping (value = "/forgot/{userEmail}")
    public ResponseEntity<ControllerResponse> forgetPassword(
            @ApiParam(value = "User Email", required = true) @PathVariable(value = "userEmail") String userEmail) {

        ControllerResponse response = new ControllerResponse();
        HttpStatus httpStatus = null;
        try {
            LOG.info("Calling service to forgot password");

            User existUser = userService.findByEmail(userEmail.trim());

            if(existUser != null)
            {
                new EmailUtility().sendEmail(
                        existUser.getEmail(),
                        "Reset Password",
                        "http://localhost:4200/#/reset-password/"+existUser.getUsername());

                httpStatus = HttpStatus.OK;
                response.setMessage(Constants.EMAIL_SENT_MESSAGE);
            }
            else {
                httpStatus =HttpStatus.NOT_FOUND;
                response.setMessage(Constants.EMAIL_NOT_EXIST_MESSAGE);
            }

        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            response.setMessage(Constants.EXCEPTION_MESSAGE);
            response.setDescription(e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(response, httpStatus);
    }

    @ApiOperation(value = "Reset Password", nickname = "resetPassword", notes = "Reset Password",
            tags = {"User"}, response = ControllerResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Data is successfully inserted"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Exception occurs while serving the request")})
    @PutMapping(value = "/resetPassword/{username}", produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<ControllerResponse> resetPassword(
            @ApiParam(value = "User Name", required = true) @PathVariable(value = "username") String username,
            @ApiParam(value = "User Object", required = true) @Valid @RequestBody User userDetails) {

        ControllerResponse response = new ControllerResponse();
        HttpStatus httpStatus = null;
        try {
            LOG.info("Calling service to reset password");

            User existUser = userService.findByUsername(username);

            if(existUser != null)
            {
                existUser.setPassWord(passwordEncode.encode(userDetails.getPassWord()));
                userService.updateUser(existUser);

                httpStatus = HttpStatus.OK;
                response.setMessage(Constants.UPPDATE_MESSAGE);
            }
            else {
                httpStatus =HttpStatus.NOT_FOUND;
                response.setMessage(Constants.EMAIL_NOT_EXIST_MESSAGE);
            }

        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            response.setMessage(Constants.EXCEPTION_MESSAGE);
            response.setDescription(e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(response, httpStatus);
    }

    @ApiOperation(value = "Find all users", nickname = "findAllUsers", notes = "Find all users",
            tags = { "User" }, response = ControllerResponse.class,
            authorizations = { @Authorization(value="jwtToken") }) // value="jwtToken" picked from swagger config
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operation Successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 204, message = "No data exist"),
            @ApiResponse(code = 500, message = "Exception occurs while retrieving the request") })
    @GetMapping(value = "/users/all", produces = { "application/json"})
    public ResponseEntity<ControllerResponse> findAllUsers()
    {
        ControllerResponse response = new ControllerResponse();
        HttpStatus httpStatus = null;
        try
        {
            LOG.info("Calling service to retrieve all users");

            List<User> usersList = userService.getAllUsers();

            response.setUsersList(usersList);
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

    @ApiOperation(value = "Find user", nickname = "findUserUsingUserName", notes = "Find user using user name",
            tags = { "User" }, response = ControllerResponse.class,
            authorizations = { @Authorization(value="jwtToken") }) // value="jwtToken" picked from swagger config
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operation Successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Data not found"),
            @ApiResponse(code = 500, message = "Exception occurs while retrieving the request") })
    @Produces({"application/json"})
    @GetMapping(value = "/user/search/{username}")
    public ResponseEntity<ControllerResponse> findUserUsingUserName(
            @ApiParam(value = "User Name", required = true) @PathVariable(value = "username") String username)
    {
        ControllerResponse response = new ControllerResponse();
        HttpStatus httpStatus = null;
        try
        {
            LOG.info("Calling service for find User using user name");
            User user = userService.findByUsername(username);
            if(user != null)
            {
                httpStatus =HttpStatus.OK;
                response.setUser(user);
            }
            else {
                httpStatus =HttpStatus.NOT_FOUND;
                response.setMessage(Constants.NOTEXIST_MESSAGE);
            }
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

    @ApiOperation(value = "Find user", nickname = "findUserUsingUserEmail", notes = "Find user using user email",
            tags = { "User" }, response = ControllerResponse.class,
            authorizations = { @Authorization(value="jwtToken") }) // value="jwtToken" picked from swagger config
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operation Successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Data not found"),
            @ApiResponse(code = 500, message = "Exception occurs while retrieving the request") })
    @Produces({"application/json"})
    @GetMapping(value = "/user/find/{email}")
    public ResponseEntity<ControllerResponse> findUserUsingUserEmail(
            @ApiParam(value = "User Email", required = true) @PathVariable(value = "email") String email)
    {
        ControllerResponse response = new ControllerResponse();
        HttpStatus httpStatus = null;
        try
        {
            LOG.info("Calling service for find User using user email");
            User user = userService.findByEmail(email);
            if(user != null)
            {
                httpStatus =HttpStatus.OK;
                response.setUser(user);
            }
            else {
                httpStatus =HttpStatus.NOT_FOUND;
                response.setMessage(Constants.NOTEXIST_MESSAGE);
            }
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
