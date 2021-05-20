package com.tweetapp.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.tweetapp.model.User;
import com.tweetapp.service.UserService;

@RestController
@CrossOrigin
@Slf4j
public class ResetPasswordController {

    @Autowired
    private UserService userService;

    @GetMapping("/api/v1.0/tweets/forgot/{username}")
    public String resetPassword(@PathVariable String username)
            throws UsernameNotFoundException {
        try {
            return userService.resetPassword(username);
        }catch (UsernameNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/api/v1.0/tweets/forgot/change/{username}")
    public String changePassword(@PathVariable String username, @RequestBody User user){
        try {
            userService.changePassword(username, user);
            return "Password Changed Successfully";
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not found");
        }
    }
}
