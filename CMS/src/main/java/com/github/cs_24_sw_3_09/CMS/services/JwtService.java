package com.github.cs_24_sw_3_09.CMS.services;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import com.github.cs_24_sw_3_09.CMS.services.serviceImpl.JwtServiceImpl.TOKEN_TYPE;

import java.util.Date;
import java.util.function.Function;

public interface JwtService {
    public String generateToken(String email, TOKEN_TYPE type);
    public String extractEmail(String token);
    public Date extractExpiration(String token);
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver);
    public Boolean validateToken(String token, UserDetails userDetails);
    public Boolean validateResetToken(String token, String email);
}
