package com.leaderboard.scoreservice.dto.response;

import lombok.Data;

import java.time.Instant;

@Data
public class ScoreEvent {

    private String  userId;

    private Integer score;

    private Instant timeStamp;

}
