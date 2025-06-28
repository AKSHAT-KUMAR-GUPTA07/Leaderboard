package com.leaderboard.scoreservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ScoreRequest {

    @NotNull
    @Min(value = 0, message = "score must be higher than 0")
    private Integer score;
}
