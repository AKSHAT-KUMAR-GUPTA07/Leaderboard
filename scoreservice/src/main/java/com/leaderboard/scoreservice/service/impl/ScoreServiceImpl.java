package com.leaderboard.scoreservice.service.impl;

import com.leaderboard.scoreservice.dto.ScoreRequest;
import com.leaderboard.scoreservice.dto.response.ScoreEvent;
import com.leaderboard.scoreservice.kafka.ScoreEventProducer;
import com.leaderboard.scoreservice.service.interfaces.ScoreSubmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScoreServiceImpl implements ScoreSubmissionService {

    private final ScoreEventProducer scoreEventProducer;

    @Override
    public void submitScore(ScoreRequest scoreRequest) {

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        ScoreEvent scoreEvent = new ScoreEvent();
        scoreEvent.setScore(scoreRequest.getScore());
        scoreEvent.setUserId(userName);

        scoreEventProducer.sendScore(scoreEvent);
    }
}
