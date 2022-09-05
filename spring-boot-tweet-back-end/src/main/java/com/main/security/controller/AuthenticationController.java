package com.main.security.controller;

import com.main.security.exception.AuthenticationException;
import com.main.security.model.AuthenticationRequest;
import com.main.security.model.AuthenticationResponse;
import com.main.security.service.MyUserDetailsService;
import com.main.security.service.impl.UserServiceImpl;
import com.main.security.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping(value = "/tweets")
@Tag(name = "Token")
@Slf4j
public class AuthenticationController {

    private JwtUtil jwtTokenUtil;
    private AuthenticationManager authenticationManager;
    private MyUserDetailsService userDetailsService;
    private UserServiceImpl userService;

    @Autowired
    public AuthenticationController(final AuthenticationManager authenticationManager,
                                    final MyUserDetailsService userDetailsService,
                                    final JwtUtil jwtTokenUtil,
                                    final UserServiceImpl userService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }

    @Operation(summary = "Generate jwt token after verify login credentials")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Generate token"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Exception occurred while serving the request")})
    @PostMapping(value = "/authenticate", produces = {"application/json"}, consumes = {"application/json"})
    public AuthenticationResponse createAuthenticationToken
            (@RequestBody AuthenticationRequest authenticationRequest) throws AuthenticationException {

            authenticate(authenticationRequest.getUsername().trim(),
                authenticationRequest.getPassword());

        // Reload password post-security so we can generate the token
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername().trim());
        final String jwtToken = jwtTokenUtil.generateToken(userDetails);
        String refreshedToken = jwtTokenUtil.refreshToken(jwtToken);

        // Return the token
        return new AuthenticationResponse(refreshedToken);
    }

    /**
     * Authenticates the user. If something is wrong, an {@link AuthenticationException} will be thrown
     */
    public void authenticate(String username, String password) {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            log.error("error: ", e.getMessage(), e);
            throw new AuthenticationException("Disabled User");
        } catch (BadCredentialsException e) {
            log.error("error: ", e.getMessage(), e);
            throw new AuthenticationException("Incorrect Credentials");
        }
    }
}
