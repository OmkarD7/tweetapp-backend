package com.tweetapp.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.tweetapp.exceptions.NotAuthorizedException;
import com.tweetapp.exceptions.ResourceNotFoundException;
import com.tweetapp.model.Tweet;
import com.tweetapp.service.SequenceGeneratorService;
import com.tweetapp.service.TweetsService;

import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin
@Slf4j
public class TweetController {

    /*private static final String TOPIC = "tweet_topic";

    @Autowired
    private KafkaTemplate<String, Tweet> kafkaTemplate;*/

    @Autowired
    private TweetsService tweetsService;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    @GetMapping("/api/v1.0/tweets/all")
    public List<Tweet> getAllTweets() {
        return tweetsService.getAllTweets();
    }

    @GetMapping("/api/v1.0/tweets/{username}")
    public List<Tweet> getTweetsByUserName(@PathVariable String username) {
        return tweetsService.getTweetsByUserName(username);
    }


    @PostMapping("/api/v1.0/tweets/{username}/add")
    public Tweet postTweet(@RequestBody Tweet tweet, @PathVariable String username) {
        try {
            //generate sequence
            tweet.setId(sequenceGeneratorService.getSequenceNumber(Tweet.SEQUENCE_NAME));
            tweet.setTimeOfTweet(new Date());

            tweetsService.postTweet(username, tweet);

            //send tweet to kafka topic
            //kafkaTemplate.send(TOPIC, tweet);
            return tweet;
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to post tweet");
        }
    }

    @DeleteMapping("/api/v1.0/tweets/{username}/delete/{id}")
    public int deleteTweet(@PathVariable String username, @PathVariable int id) {
        try {
            String res = tweetsService.deleteTweet(id, username);
            log.info(res);
            return id;
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to delete tweet");
        }
    }

    @PutMapping("/api/v1.0/tweets/{username}/update/{id}")
    public String updateTweet(@RequestBody Tweet tweet, @PathVariable String username,
                              @PathVariable int id) throws Exception{
        if (username.equals(tweet.getUserName())){
        	return tweetsService.updateTweet(id, username, tweet);
        }else {
            throw new NotAuthorizedException("You are not allowed to update this tweet");
        }
    }

    @PutMapping("/api/v1.0/tweets/{username}/like/{id}")
    public void likeTweet(@PathVariable String username,
                            @PathVariable int id,
                            @RequestBody Tweet tweet) throws ResourceNotFoundException {
         try {
			tweetsService.likeTweet(username, id, tweet);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to like tweet");
		}
    }

    @PostMapping("/api/v1.0/tweets/{username}/reply/{id}")
    public void replyToTweet(@PathVariable String username,  @PathVariable int id, @RequestBody Tweet reply){
    	try {
    		reply.setId(sequenceGeneratorService.getSequenceNumber(Tweet.SEQUENCE_NAME));
    		reply.setTimeOfTweet(new Date());
    		tweetsService.replyTweet(username, id, reply);
    	}catch(ResourceNotFoundException e) {
    		e.printStackTrace();
    		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tweet Not Found");
    	}
    }

}
