
package com.tweetapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.tweetapp.model.RegistrationResponse;
import com.tweetapp.model.User;
import com.tweetapp.service.SequenceGeneratorService;
import com.tweetapp.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    @GetMapping("/api/v1.0/tweets/users/all")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @PostMapping("/api/v1.0/tweets/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        RegistrationResponse res = new RegistrationResponse();
        try {
            //log.info("User in controller: "+ user.toString());
            user.setId(sequenceGeneratorService.getSequenceNumber(User.SEQUENCE_NAME));
            user.setRoles("USER");
            //user.setActive(true);
            user.setConfirmed(true);
            userService.registerUser(user);
            res.setSuccess(true);
            res.setResponseMessage("Registration Successful.");
        }catch (Exception e){
            res.setSuccess(false);
            res.setResponseMessage("Registration Failed.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return ResponseEntity.ok(res);
    }

    @GetMapping("/api/v1.0/tweets/user/search/{username}")
    public User getUserByUserName(@PathVariable String username) throws UsernameNotFoundException {
        try {
            User user = userService.getUserByUserName(username);
            return new User(user);
            //return user.map(User::new).get();
        }catch (UsernameNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/api/v1.0/tweets/user/{username}")
    public int getUserIdByUserName(@PathVariable String username) throws UsernameNotFoundException{
        int userId = userService.getUserIdByUserName(username);
        return userId;
    }

    @GetMapping("/api/v1.0/tweets/user/confirm/{id}/{emailId}")
    public String confirmUser(@PathVariable int id, @PathVariable String emailId){
        try {
            userService.confirmUser(id, emailId);
            return "EMAIL CONFIRMATION SUCCESSFUL";
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Request");
        }
    }

    @GetMapping("/api/v1.0/tweets/users/{id}")
    public User getUserById(@PathVariable int id){
        try{
            return userService.getUserById(id);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Request");
        }
    }
}
