package com.main.security.controller;

import com.main.security.model.User;
import com.main.security.service.impl.UserServiceImpl;
import com.main.util.EmailUtil;
import com.microsoft.azure.servicebus.primitives.StringUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/tweets")
@Tag(name = "User")
public class UserController {

    private UserServiceImpl userService;
    private EmailUtil emailUtil;
    private AuthenticationManager authenticationManager;

    @Autowired
    public UserController(final UserServiceImpl userService,
                          final EmailUtil emailUtil,
                          final AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.emailUtil = emailUtil;
        this.authenticationManager = authenticationManager;
    }

    @Operation(summary = "This operation is used to create user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Creation of user"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Exception occurred while serving the request")})
    @PostMapping(value = "/register", produces = {"application/json"},
            consumes = {"application/json"})
    public ResponseEntity<User> saveUser(@Valid @RequestBody User user) throws MessagingException {
        if(userService.isUserExist(user.getUsername())) {
            throw new RuntimeException("User exist");
        }
        User userA = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userA);
    }

    @Operation(summary = "This operation is used to update user",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update user"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Exception occurred while serving the request")})
    @PutMapping(value = "/updateUser", produces = {"application/json"},
            consumes = {"application/json"})
    public ResponseEntity<User> updateUser(@Valid @RequestBody User user) {
        User userA = userService.findUser(user.getUsername());
        if (userA != null) {
            userA.setEmail(user.getEmail());
            userA.setPhoneNumber(user.getPhoneNumber());
            userA.setModifiedBy(user.getModifiedBy());
            if(userA.getRoles().equals("ADMIN") && !StringUtil.isNullOrEmpty(user.getRoles())) {
                userA.setRoles(user.getRoles());
            }
            userService.updateUser(userA);
        }
        return ResponseEntity.status(HttpStatus.OK).body(userA);
    }

    @Operation(summary = "This operation is used to update user password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update user password"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Exception occurred while serving the request")})
    @PutMapping(value = "/updateUserPassword", produces = {"application/json"},
            consumes = {"application/json"})
    public ResponseEntity<User> updateUserPassword(@Valid @RequestBody User user) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                user.getUsername(), user.getPassword()));
        User userA = userService.findUser(user.getUsername());
        if (userA != null) {
            userA.setPassword(user.getUpdatedPassword());
            userA.setModifiedBy(user.getModifiedBy());
            userService.updateUser(userA);
        }
        return ResponseEntity.status(HttpStatus.OK).body(userA);
    }

    @Operation(summary = "This operation is used to find an user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Find an user"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Exception occurred while serving the request")})
    @GetMapping(value = "/findAnUser/{userName}", produces = {"application/json"})
    public ResponseEntity<User> findAnUser(@PathVariable(value = "userName") String userName) {
        User user = userService.findUser(userName);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @Operation(summary = "This operation is used to find an user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Find an user"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Exception occurred while serving the request")})
    @GetMapping(value = "/findAnUserForNotAdmin/{username}/{systemUser}", produces = {"application/json"})
    public ResponseEntity<User> findAnUserForNotAdmin(@PathVariable(value = "username") String username,
                                                      @PathVariable(value = "systemUser") String systemUser) {
        User user = userService.findUserForNotAdmin(username, systemUser);
        if(user == null)
        {
            throw new UsernameNotFoundException("User not exist!!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @Operation(summary = "This operation is used to delete an user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delete an user"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Exception occurred while serving the request")})
    @DeleteMapping(value = "/deleteAnUser/{userName}")
    public ResponseEntity<User> deleteAnUser(@PathVariable(value = "userName") String userName) {
        User user = userService.findUser(userName); // if user not exist, it will throw error here
        userService.deleteUser(user);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
