package com.leaderboard.scoreservice.security;

import com.leaderboard.scoreservice.utility.JwtUtil;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
          @Nonnull HttpServletRequest request,
          @Nonnull  HttpServletResponse response,
          @Nonnull  FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            response.sendError(HttpStatus.UNAUTHORIZED.value() , "Invalid token");
            log.info("invalid token die to authHeader error");
            return;
        }

        try{
            String token = authHeader.substring(7);
            if( !jwtUtil.isValidToken(token)){
                log.info("invalidity of the token checked");
                response.sendError(HttpStatus.UNAUTHORIZED.value() , "Invalid token");
                return;
            }
            String username = jwtUtil.extractUserName(token);
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(username, null, List.of());

            SecurityContextHolder.getContext().setAuthentication(authToken);

            filterChain.doFilter(request,response);

        } catch (Exception e) {
            log.error("JWT Authentication failed: {}", e.getMessage());
            response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value() , "Authentication Failed");
        }

    }
}
