package com.leaderboard.scoreservice.utility;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;


@Component
@Slf4j
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    private SecretKey getSignInKey(){
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    private Claims extractAllClaims(String token){
        try{
            return Jwts.parser()
                    .verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            throw new RuntimeException("Invalid Jwt: "+e.getMessage());
        }
    }

    public <T> T extractClaim(String token , Function<Claims , T> claimsResolver){
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractUserName(String token){
        return extractClaim(token , Claims::getSubject);
    }

    public boolean isValidToken(String token){
        try{
            log.info("checking if the token is valid");
            Claims claims = extractAllClaims(token);
            log.info("extracted claims: {}",claims);
            boolean isExpired = isTokenExpired(token);
            if(isExpired){
                log.info("Token is expired");
            }
            return !isExpired;
        } catch (JwtException | IllegalArgumentException e) { // Catch specific exceptions
            log.warn("Invalid token: {}", e.getMessage()); // Warn instead of Info (security)
            return false; // Instead of throwing, return false (if validation is expected to return boolean)
            // OR throw a custom exception (if the method should fail fast)
            // throw new InvalidTokenException("Token validation failed", e);
        } catch (Exception e) {
            log.info("validation failed : {}",e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private Date extractExpiration(String token){
        return extractClaim(token , Claims::getExpiration);
    }

    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

}
