package com.leaderboard.scoreservice.controller;

import com.leaderboard.scoreservice.dto.ScoreRequest;
import com.leaderboard.scoreservice.service.impl.ScoreServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/scores")
@RequiredArgsConstructor
@Slf4j
public class ScoreController {

    private final ScoreServiceImpl scoreService;

    @PostMapping
    public ResponseEntity<String> submitScore(@RequestBody ScoreRequest scoreRequest){

        scoreService.submitScore(scoreRequest);

        return new ResponseEntity<>("Score submitted",HttpStatus.OK);
    }

}
