package com.example.ConnectService.service;

import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    public SecretKey getSigningKey(){

        byte[] byteKey = Decoders.BASE64.decode(secret);

        return Keys.hmacShaKeyFor(byteKey);
    }

    public String extractUsername(String token){
        return extractClaims(token,
            Claims::getSubject);
    }

    public Long extractUserId(String token){
        return extractClaims(token,
            Claims -> Claims.get("userId", Long.class));
    }

    public Date extractExpiration(String token){
        return extractClaims(token,
            Claims::getExpiration
        );
    }

    public <T> T extractClaims(String token, Function<Claims,T> resolver){

        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);

    }

    private Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(
                    getSigningKey()
                ).build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean ValidateToken(String token){
        try {
            Claims claims = extractAllClaims(token);

            return claims.getExpiration() == null || 
            claims.getExpiration().after(new Date());
        } catch (Exception e) {
            System.out.println(
                "Inalid token" + e.getMessage()
            );
            return false;
        }
    }
    
}
