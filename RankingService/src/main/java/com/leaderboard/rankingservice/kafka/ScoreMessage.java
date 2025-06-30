package com.leaderboard.rankingservice.kafka;

import lombok.Data;

import java.time.Instant;

@Data
public class ScoreMessage {

    private String userId;
    private int score;
    private Instant timeStamp;
}
