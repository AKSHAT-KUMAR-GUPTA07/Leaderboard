package com.leaderboard.scoreservice.dto.response;

import lombok.Data;

@Data
public class ScoreEvent {

    private String  userId;

    private Integer score;

}
