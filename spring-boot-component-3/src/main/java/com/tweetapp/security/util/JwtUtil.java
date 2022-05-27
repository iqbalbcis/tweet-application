package com.tweetapp.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClock;
import io.jsonwebtoken.Clock;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private Clock clock = DefaultClock.INSTANCE;

    @Value("${jwt.signing.key.secret}")
    private String secretKey;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Boolean ignoreTokenExpiration(String token) {
        // here you specify tokens, for that the expiration is ignored
        return false;
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {

        // subeject is userName
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(calculateExpirationDate())
                .signWith(SignatureAlgorithm.HS256, secretKey).compact();
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();

        if (roles.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            claims.put("isAdmin", true);
        }
        if (roles.contains(new SimpleGrantedAuthority("ROLE_USER"))) {
            claims.put("isUser", true);
        }
        
        return doGenerateToken(claims, userDetails.getUsername());
    }

    //this is only for admin control purpose
    public String generateTokenForAdmin(String adminName) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("isAdmin", true);
        return doGenerateToken(claims, adminName);
    }

    private Date calculateExpirationDate() {
        // 1000 * 60 * 60 * 24 // 24 hours
        return new Date(System.currentTimeMillis() + 1000 * 60 * 10); // 24 hours
    }

    private Date calculateExpirationDateForRefreshToken() {
        // 1000 * 60 * 60 * 24 // 24 hours
        return new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24); // 24 hours
    }

    private Date calculateExpirationDateForResetToken() {
        // 1000 * 60 * 60 * 24 // 24 hours
        return new Date(System.currentTimeMillis() + 1000 * 60 * 60 ); // 1 hours
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public Boolean canTokenBeRefreshed(String token) {
        return (!isTokenExpired(token) || ignoreTokenExpiration(token));
    }

    public String refreshToken(String token) {

        final Date createdDate = clock.now();
        final Date expirationDate = calculateExpirationDateForRefreshToken();

        final Claims claims = extractAllClaims(token);
        claims.setIssuedAt(createdDate);
        claims.setExpiration(expirationDate);

        return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS256, secretKey).compact();
    }

    public String resetToken(String token) {

        final Date createdDate = clock.now();
        final Date expirationDate = calculateExpirationDateForResetToken();

        final Claims claims = extractAllClaims(token);
        claims.setIssuedAt(createdDate);
        claims.setExpiration(expirationDate);

        return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS256, secretKey).compact();
    }

    //===========================================================================================
}
