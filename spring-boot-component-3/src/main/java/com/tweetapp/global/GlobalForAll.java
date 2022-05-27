package com.tweetapp.global;

import com.tweetapp.security.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class GlobalForAll {

    @Value("${jwt.http.request.header}")
    private String tokenHeader;

    private JwtUtil jwtTokenUtil;

    @Autowired
    public GlobalForAll(final JwtUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public String getEventPerformerName(HttpServletRequest request) {
        final String token = getJwt(request);
        String username = jwtTokenUtil.extractUsername(token);
        return username;
    }

    public String getJwt(HttpServletRequest request) {
        //String authHeader = request.getHeader("Authorization");
        String authHeader = request.getHeader(tokenHeader);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.replace("Bearer ", "");
        }

        return null;
    }
}
