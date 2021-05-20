package com.tweetapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.tweetapp.model.AuthenticationRequest;
import com.tweetapp.service.MyUserDetailsService;
import com.tweetapp.service.UserService;
import com.tweetapp.util.JwtUtil;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthenticationController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private MyUserDetailsService myUserDetailsService;
    @Autowired
    private JwtUtil jwtUtil;


    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST, path = "/api/v1.0/tweets/login")
    public Map<String, String> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest)
            throws Exception{
        Map<String, String> authMap = new HashMap<String, String>();
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUserName(),
                            authenticationRequest.getPassword()));
        }catch (BadCredentialsException e){
            //return ResponseEntity.ok(new AuthenticationResponse(false, null));
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = myUserDetailsService.
                loadUserByUsername(authenticationRequest.getUserName());

        final String jwt = jwtUtil.generateToken(userDetails);
        final String role = SecurityContextHolder.getContext()
                .getAuthentication().getAuthorities().toArray()[0].toString();
        authMap.put("token", jwt);
        authMap.put("role", role);

        return authMap;
    }
}
