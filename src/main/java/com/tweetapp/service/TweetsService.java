package com.tweetapp.service;

import java.util.ArrayList;
import java.util.*;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.tweetapp.exceptions.ResourceNotFoundException;
import com.tweetapp.model.Tweet;
import com.tweetapp.model.User;
import com.tweetapp.repository.TweetRepository;
import com.tweetapp.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;
@Service
@Slf4j
public class TweetsService {
    @Autowired
    TweetRepository  tweetRepository;
    
    @Autowired
    UserRepository userRepository;

    public List<Tweet> getAllTweets() {
    	Comparator<Tweet> dateComparator = Comparator
    			.comparing(Tweet::getTimeOfTweet, Comparator.nullsFirst(Date::compareTo)).reversed();
        return tweetRepository.findAll().stream().sorted(dateComparator).collect(Collectors.toList());
    }

    public void postTweet(String username, Tweet tweet) {
    	if(userRepository.findByUserName(username).isPresent()) {
        tweetRepository.save(tweet);
        log.info(tweet.toString());
        log.info("Tweet Posted  for user: "+ tweet.getUserName());
    	}else {
    		throw new UsernameNotFoundException("User Name not found: "+ username);
    	}
    }

    public String updateTweet(int id, String userName, Tweet tweet) throws Exception {
    	 if(userRepository.findByUserName(userName).isPresent()) {
	       Tweet retrievedTweet = tweetRepository.findById(id)
	               .orElseThrow(() -> new ResourceNotFoundException("Tweet Not found"));
	       retrievedTweet.setTweetBody(tweet.getTweetBody());
	       final Tweet updatedTweet = tweetRepository.save(retrievedTweet);
	       return "tweet updated "+ retrievedTweet.getTweetBody();
    	 }else {
    		 throw new UsernameNotFoundException("User Name not found: "+ userName);
    	 }
    }

    public String deleteTweet(int id, String username) {
        tweetRepository.deleteById(id);
        return "Tweet with ID: "+ id +" Deleted by user: "+username;
    }

    public List<Tweet> getTweetsByUserName(String username) {
    	Comparator<Tweet> dateComparator = Comparator
    			.comparing(Tweet::getTimeOfTweet, Comparator.nullsFirst(Date::compareTo)).reversed();
        return tweetRepository.findByUserName(username).stream().sorted(dateComparator).collect(Collectors.toList());
    }

    public String likeTweet(String username, int id, Tweet tweet) throws Exception {
        Tweet retrievedTweet = tweetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tweet Not found"));
        retrievedTweet.setLikedBy(tweet.getLikedBy());
        retrievedTweet.setNumberOfLikes(tweet.getNumberOfLikes());
        tweetRepository.save(retrievedTweet);
        
        
        return "Total Number of Likes for tweet id: "+ id+" = "+retrievedTweet.getNumberOfLikes();
    }

	public String replyTweet(String username, int tweetId, Tweet reply) throws ResourceNotFoundException{
		// TODO Auto-generated method stub
		Optional<User> userOpt =  userRepository.findByUserName(username);
		
		if(userOpt.isPresent()) {
			Tweet originalTweet = tweetRepository.findById(tweetId).
					orElseThrow(() -> new ResourceNotFoundException("Tweet Not Founnd"));
			//originalTweet.setReplies(tweet.getReplies());
			if(CollectionUtils.isEmpty(originalTweet.getReplies())) {
				List<Tweet> replies = new ArrayList<Tweet>();
				replies.add(reply);
				originalTweet.setReplies(replies);
			}else {
				originalTweet.getReplies().add(reply);
			}
			tweetRepository.save(originalTweet);
		}
		
		return null;
	}
}
