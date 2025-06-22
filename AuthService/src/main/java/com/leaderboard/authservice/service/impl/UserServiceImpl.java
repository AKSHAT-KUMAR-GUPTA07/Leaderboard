package com.leaderboard.authservice.service.impl;

import com.leaderboard.authservice.dto.UserLoginRequest;
import com.leaderboard.authservice.dto.UserRegisterRequest;
import com.leaderboard.authservice.dto.response.LoginResponseDto;
import com.leaderboard.authservice.entity.User;
import com.leaderboard.authservice.repository.UserRepository;
import com.leaderboard.authservice.service.interfaces.UserServiceInterface;
import com.leaderboard.authservice.utilities.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserServiceInterface {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    public User saveUser(UserRegisterRequest request) {

        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepo.save(newUser);
        log.info("user saved: {}",newUser);

        return newUser;
    }

    @Override
    public LoginResponseDto findByUsername(UserLoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userRepo.findByUsername(request.getUsername()).orElseThrow(() ->
                new RuntimeException("user not found with the username: "+request.getUsername()));

        String token = jwtUtil.generateToken(user.getUsername());
        long expire = jwtUtil.getExpirationTime();
        return new LoginResponseDto(token,expire);
    }
}
