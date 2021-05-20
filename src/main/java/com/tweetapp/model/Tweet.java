package com.tweetapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Tweet")
public class Tweet {
    @Transient
    public static final String SEQUENCE_NAME = "tweet_sequence";

    @Id
    private int id;
    private String userName;
    private String tweetBody;
    private int numberOfLikes;
    private List<String> likedBy;
    
    private List<Tweet> replies;
    private Date timeOfTweet;
}
