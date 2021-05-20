package com.tweetapp;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import com.tweetapp.controller.UserController;
import com.tweetapp.repository.TweetRepository;
import com.tweetapp.repository.UserRepository;
import com.tweetapp.service.TweetsService;
import com.tweetapp.service.UserService;

@Profile("test")
@Configuration
public class TweetAppTestConfig {

	@Bean
	@Primary
	public TweetsService testTweetService() {
		return Mockito.mock(TweetsService.class);
	}

	@Bean
	@Primary
	public UserService testUserService() {
		return Mockito.mock(UserService.class);
	}


	@Bean
	@Primary public TweetRepository testTweetRepo() { return
	Mockito.mock(TweetRepository.class); }
	  
	@Bean
	@Primary public UserRepository testUserRepo() { return
	Mockito.mock(UserRepository.class); }
	 

}
