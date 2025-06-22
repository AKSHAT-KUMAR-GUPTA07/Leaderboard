package com.leaderboard.authservice.dto.response;

import lombok.AllArgsConstructor;

import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDto {

    private String token;
    private long expiresIn;

}
