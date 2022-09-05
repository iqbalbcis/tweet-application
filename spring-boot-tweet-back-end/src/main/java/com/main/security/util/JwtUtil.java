package com.main.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClock;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static com.main.constants.CommonConstant.SECRETKEY;

@Component
public class JwtUtil {

    private Clock clock = DefaultClock.INSTANCE;

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
        return Jwts.parser().setSigningKey(SECRETKEY).parseClaimsJws(token).getBody();
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
                .signWith(SignatureAlgorithm.HS256, SECRETKEY).compact();
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        //===========================================
//        List<String> roles = new ArrayList<>();
//
//        if (userDetails.getAuthorities() != null) {
//            for (GrantedAuthority authority : userDetails.getAuthorities()) {
//                roles.add(authority.getAuthority());
//            }
//        }
//
//        claims.put("roles", roles);
        //============================================
//        Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();
//
//        if (roles.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
//            claims.put("isAdmin", true);
//        }
//        if (roles.contains(new SimpleGrantedAuthority("ROLE_USER"))) {
//            claims.put("isUser", true);
//        }
//        claims.put("userId", String.valueOf(jwtUser.getId()));
//        claims.put("role", jwtUser.getRole());
        //============================================

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
        return new Date(System.currentTimeMillis() + 1000 * 60 * 3); // 3 minutes
    }

    private Date calculateExpirationDateForRefreshToken() {
        // 1000 * 60 * 60 * 24 // 24 hours
        return new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 72); // 72 hours
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

        return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS256, SECRETKEY).compact();
    }

//    public List<SimpleGrantedAuthority> getRolesFromToken(String token) {
//        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
//
//        List<SimpleGrantedAuthority> roles = null;
//
//        Boolean isAdmin = claims.get("isAdmin", Boolean.class);
//        Boolean isUser = claims.get("isUser", Boolean.class);
//
//        if (isAdmin != null && isAdmin) {
//            roles = Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
//        }
//
//        if (isUser != null && isAdmin) {
//            roles = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
//        }
//        return roles;
//
//    }

    //===========================================================================================
}
