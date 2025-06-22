package com.leaderboard.authservice.service.interfaces;

import com.leaderboard.authservice.dto.UserLoginRequest;
import com.leaderboard.authservice.dto.UserRegisterRequest;
import com.leaderboard.authservice.dto.response.LoginResponseDto;
import com.leaderboard.authservice.entity.User;


public interface UserServiceInterface {

    User saveUser(UserRegisterRequest request);

    LoginResponseDto findByUsername(UserLoginRequest request);
}
