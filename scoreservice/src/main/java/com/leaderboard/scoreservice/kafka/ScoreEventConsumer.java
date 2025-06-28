package com.leaderboard.scoreservice.kafka;

import com.leaderboard.scoreservice.dto.ScoreRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ScoreEventConsumer {

    @KafkaListener(topics = "score-submitted", groupId = "score-consumer-group")
    public void listen(ConsumerRecord<String,String> response){
        log.info("received messages from kafka: {}",response.value());
    }
}
