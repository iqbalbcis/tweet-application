package com.tweetapp.security.controller;

import com.tweetapp.global.GlobalForAll;
import com.tweetapp.security.exception.AuthenticationException;
import com.tweetapp.security.model.AuthenticationRequest;
import com.tweetapp.security.model.AuthenticationResponse;
import com.tweetapp.security.model.User;
import com.tweetapp.security.service.MyUserDetailsService;
import com.tweetapp.security.service.impl.UserServiceImpl;
import com.tweetapp.security.util.JwtUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@RestController
@RequestMapping(value = "/api/v1.0/tweets")
public class AuthenticationController {

    @Value("${jwt.http.request.header}")
    private String tokenHeader;

    private JwtUtil jwtTokenUtil;
    private AuthenticationManager authenticationManager;
    private MyUserDetailsService userDetailsService;
    private GlobalForAll global;
    private UserServiceImpl userService;

    @Autowired
    public AuthenticationController(final AuthenticationManager authenticationManager,
                                    final MyUserDetailsService userDetailsService,
                                    final JwtUtil jwtTokenUtil,
                                    final GlobalForAll global,
                                    final UserServiceImpl userService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.global = global;
        this.userService = userService;
    }

    @ApiOperation(value = "Validate login and produce JWT token", nickname = "authenticate", notes = "Produce JWT token",
            tags = { "Token" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operation Successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Exception occurs while serving the request") })
    @PostMapping(value = "/login", produces = { "application/json"}, consumes= { "application/json"} )
    public ResponseEntity<?> validateLonginAndCreateToken
            (@RequestBody AuthenticationRequest authenticationRequest) throws AuthenticationException {

        User user = userService.findByUsername(authenticationRequest.getUsername());
        if(user != null)
        {
            authenticate(authenticationRequest.getUsername(),
                    authenticationRequest.getPassWord());

            // Reload password post-security so we can generate the token
            final UserDetails userDetails = userDetailsService
                    .loadUserByUsername(authenticationRequest.getUsername());
            final String jwt = jwtTokenUtil.generateToken(userDetails);

            // Return the token
            return ResponseEntity.ok(new AuthenticationResponse(jwt));
        }
        else {
            return ResponseEntity.badRequest().body("Mismatch information");
        }
    }

    @ApiOperation(value = "Refresh JWT token", nickname = "refresh", notes = "Refresh JWT token",
            tags = { "Token" }, authorizations = { @Authorization(value="jwtToken") }) // value="jwtToken" picked from swagger config
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operation Successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Exception occurs while serving the request") })
    @GetMapping(value = "/refresh", produces = { "application/json"})
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {

        final String token = global.getJwt(request);
        String username = global.getEventPerformerName(request);
        userDetailsService.loadUserByUsername(username);

        if (jwtTokenUtil.canTokenBeRefreshed(token)) {
            String refreshedToken = jwtTokenUtil.refreshToken(token);
            return ResponseEntity.ok(new AuthenticationResponse(refreshedToken));
        }
        return ResponseEntity.badRequest().body(null);
    }

    @ApiOperation(value = "Reset JWT token", nickname = "reset", notes = "Reset JWT token",
            tags = { "Token" }, authorizations = { @Authorization(value="jwtToken") }) // value="jwtToken" picked from swagger config
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operation Successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Exception occurs while serving the request") })
    @GetMapping(value = "/resetToken", produces = { "application/json"})
    public ResponseEntity<?> resetAuthenticationToken(HttpServletRequest request) {

        final String token = global.getJwt(request);
        String username = global.getEventPerformerName(request);
        userDetailsService.loadUserByUsername(username);

        if (jwtTokenUtil.canTokenBeRefreshed(token)) {
            String resetToken = jwtTokenUtil.resetToken(token);
            return ResponseEntity.ok(new AuthenticationResponse(resetToken));
        }
        return ResponseEntity.badRequest().body(null);
    }

    @ExceptionHandler({ AuthenticationException.class })
    public ResponseEntity<String> handleAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    public void authenticate(String username, String password) {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new AuthenticationException("Disabled User", e);
        } catch (BadCredentialsException e) {
            throw new AuthenticationException("Incorrect Credentials", e);
        }
    }
}
