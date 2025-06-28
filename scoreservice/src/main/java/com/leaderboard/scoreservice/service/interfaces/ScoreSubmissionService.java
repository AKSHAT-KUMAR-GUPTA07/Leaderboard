package com.leaderboard.scoreservice.service.interfaces;

import com.leaderboard.scoreservice.dto.ScoreRequest;

public interface ScoreSubmissionService {

    void submitScore(ScoreRequest scoreRequest);
}
