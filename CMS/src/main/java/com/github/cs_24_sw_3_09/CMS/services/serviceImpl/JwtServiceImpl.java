package com.github.cs_24_sw_3_09.CMS.services.serviceImpl;

import com.github.cs_24_sw_3_09.CMS.services.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/*
 * The code in this class is inspired, taken or modified from the following:
 * https://www.geeksforgeeks.org/spring-boot-3-0-jwt-authentication-with-spring-security-using-mysql-database/ 
 */
@Service
public class JwtServiceImpl implements JwtService {

    public static enum TOKEN_TYPE {
        AUTH_TOKEN,
        RESET_TOKEN
    }

    // Get jwt secret from environment variables
    @Autowired
    private Environment environment;

    private SecretKey getSigningKey() {
        byte[] keyBytes = environment.getProperty("JWT.SECRET", "iJnfAXTr9RNZmhWrMfSLzUnlqezjw2mR").getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String email, TOKEN_TYPE type) {
        JwtBuilder tokenBuilder = Jwts.builder()
        .subject(email)
        .issuedAt(new Date());
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", type.name());
        tokenBuilder.claims(claims);
        long expiryTime = System.currentTimeMillis();
        switch(type) {
            case AUTH_TOKEN:
                expiryTime += 1000 * 60 * 60 * 24 * 7; //Valid for 7 days
            case RESET_TOKEN:
                expiryTime += 1000 * 60 * 5; //Valid for 5 minutes
        }
        tokenBuilder.expiration(new Date(expiryTime));
        return tokenBuilder.signWith(getSigningKey()).compact();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token).getPayload();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public TOKEN_TYPE extractType(String token) {
        Function<Claims, TOKEN_TYPE> typeResolver = claims -> TOKEN_TYPE.valueOf(claims.get("type", String.class));
        return extractClaim(token, typeResolver);
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Boolean isTokenType(String token, TOKEN_TYPE type) {
        return extractType(token).equals(type);
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String email = extractEmail(token);
        return (email.equals(userDetails.getUsername()) && !isTokenExpired(token) && isTokenType(token, TOKEN_TYPE.AUTH_TOKEN));
    }

    public Boolean validateResetToken(String token, String email) {
        final String extractedEmail = extractEmail(token);
        return (extractedEmail.equals(email) && !isTokenExpired(token) && isTokenType(token, TOKEN_TYPE.RESET_TOKEN));
    }
}