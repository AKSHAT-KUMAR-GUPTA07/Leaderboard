package com.leaderboard.rankingservice.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.WeekFields;

@Component
@Slf4j
@RequiredArgsConstructor
public class ScoreKafkaConsumer {

    private final ObjectMapper objectMapper;
    private final RedisTemplate<String,String> redisTemplate;

    @KafkaListener(topics = "score-submitted" , groupId = "ranking-group-V2")
    public void listen(ConsumerRecord<String,String> response){
        log.info("score Message from kafka: {}",response);
        try{
            String json = response.value();
            ScoreMessage scoreMessage = objectMapper.readValue(json , ScoreMessage.class);
            log.info("score message is: {}",scoreMessage);

            String userName = scoreMessage.getUserId();
            int score = scoreMessage.getScore();
            Instant timeStamp = scoreMessage.getTimeStamp();

            LocalDateTime time = LocalDateTime.ofInstant(timeStamp , ZoneOffset.UTC);

            // generate redis keys
            String dailyKey = "leaderboard:daily:" + time.toLocalDate(); // e.g., leaderboard:daily:2025-06-29
            String weeklyKey = "leaderboard:weekly:" + time.getYear() + "-W" + time.get(WeekFields.ISO.weekOfWeekBasedYear());
            String monthlyKey = "leaderboard:monthly:" + time.getYear() + "-" + String.format("%02d", time.getMonthValue());
            String allTimeKey = "leaderboard:alltime";

            // Increment score in all leaderboards
            redisTemplate.opsForZSet().incrementScore(dailyKey, userName, score);
            redisTemplate.opsForZSet().incrementScore(weeklyKey, userName, score);
            redisTemplate.opsForZSet().incrementScore(monthlyKey, userName, score);
            redisTemplate.opsForZSet().incrementScore(allTimeKey, userName, score);

            // Set TTL
            redisTemplate.expire(dailyKey, Duration.ofDays(10));
            redisTemplate.expire(weeklyKey, Duration.ofDays(60));
            redisTemplate.expire(monthlyKey, Duration.ofDays(90));

            log.info("Scores updated in Redis for user {}", userName);


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
