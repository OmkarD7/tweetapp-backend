package com.tweetapp.service;

import com.tweetapp.model.Tweet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumerService {
    @KafkaListener(topics = "tweets_topic", groupId = "group_id", containerFactory = "tweetContainerFactory")
    public void consumeMessage(Tweet tweet){
        log.info("Consumed tweet: "+tweet);
    }
}
