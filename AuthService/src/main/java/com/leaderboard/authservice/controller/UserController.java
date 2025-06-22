package com.leaderboard.authservice.controller;

import com.leaderboard.authservice.dto.UserLoginRequest;
import com.leaderboard.authservice.dto.UserRegisterRequest;
import com.leaderboard.authservice.dto.response.LoginResponseDto;
import com.leaderboard.authservice.service.impl.UserServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserServiceImpl userService;

    public UserController( UserServiceImpl userService){
        this.userService=userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRegisterRequest registerRequest){
        userService.saveUser(registerRequest);
        return ResponseEntity.ok("user registered");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> loginUser(@RequestBody UserLoginRequest loginRequest){
        LoginResponseDto responseDto = userService.findByUsername(loginRequest);
        return ResponseEntity.ok(responseDto);
    }
}
