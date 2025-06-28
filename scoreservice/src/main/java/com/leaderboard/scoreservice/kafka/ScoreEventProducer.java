package com.leaderboard.scoreservice.kafka;


import com.leaderboard.scoreservice.dto.response.ScoreEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScoreEventProducer {

    private final KafkaTemplate<String,Object> kafkaTemplate;
    private static final String TOPIC = "score-submitted";

    public void sendScore(ScoreEvent event){
        kafkaTemplate.send(TOPIC,event);
    }
}
