package com.tweetapp.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tweetapp.model.Tweet;

import java.util.List;

@Repository
public interface TweetRepository extends MongoRepository<Tweet, Integer> {
    List<Tweet> findByUserName(String username);

}
