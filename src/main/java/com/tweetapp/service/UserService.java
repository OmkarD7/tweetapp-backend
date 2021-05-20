package com.tweetapp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tweetapp.model.User;
import com.tweetapp.repository.UserRepository;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.Random;

@Slf4j
@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired PasswordEncoder passwordEncoder;

    private static final String SMTP_SERVER = "smtp.gmail.com";
    private static final String USERNAME = "omkardnyanmote07";
    private static final String PASSWORD = "";

    private static final String EMAIL_FROM = "omkardnyanmote07@gmail.com";
    private static final String EMAIL_TO = "";
    private static final String EMAIL_TO_CC = "";

    private static final String CONFIRM_USER_EMAIL_SUBJECT = "Email Confirmation for TweetApp";
    private static final String RESET_PASSWORD_EMAIL_SUBJECT = "Reset Password OTP for TweetApp";
    private static final String EMAIL_TEXT = "";

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public String registerUser(User user) throws Exception {
        Optional<User> userObject = userRepository.findByUserName(user.getUserName());

        if (userObject.isPresent()) {
            log.info("User already exist");
            throw new Exception("User already exist");
        } else {

            System.out.println(user);
            String password = user.getPassword();
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);

			/*
			 * String message = "Hi, " + user.getUserName() +
			 * "\nClick on the link below to confirm your email.\n" +
			 * "http://localhost:8082/api/v1.0/tweets/user/confirm/" + user.getId() + "/" +
			 * user.getEmailId();
			 * 
			 * sendEmail(CONFIRM_USER_EMAIL_SUBJECT, message, user.getEmailId());
			 */
        }
        return "User created with following details: \nuser name: " + user.getUserName() + " Email ID: " + user.getEmailId();
    }

    public User confirmUser(int id, String emailId){
        //userRepository.confirmUser(true, id, emailId);
        User newUser =userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found."));
        newUser.setConfirmed(true);
        newUser.setEmailId(emailId);
        final User finalUser = userRepository.save(newUser);
        return finalUser;
    }

    public User getUserByUserName(String username) {

        return userRepository.findByUserName(username)
        		.orElseThrow(()-> new UsernameNotFoundException("User not found: "+ username));
    }

    public int getUserIdByUserName(String username) {
        Optional<User> user = userRepository.findByUserName(username);
        User retrievedUser = new User();
        if (user.isPresent()){
            retrievedUser  =  user.map(User::new).get();
        }
        user.orElseThrow(() -> new UsernameNotFoundException("Not Found: "+ username));
        return retrievedUser.getId();
    }

    public User getUserById(int id) throws ResourceNotFoundException {
        Optional<User> user = userRepository.findById(id);
        user.orElseThrow(() -> new ResourceNotFoundException("User Not found"));
        return user.map(User::new).get();
    }

    public String resetPassword(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found: "+username));

        String otp = generateOtp();
        String message = "Hi "+ username + " Please use following OTP to reset your password."+
                "\nOTP: "+ otp;
        sendEmail(RESET_PASSWORD_EMAIL_SUBJECT, message, user.getEmailId());
        return otp;
    }

    public String generateOtp(){
        //Random random = new Random();
        //String otp  = "%04d%n"+random.nextInt(10000);
        int otp = (int)(Math.random()*9000)+1000;

        String generatedOtp = String.valueOf(otp);
        log.info("GENERATED OTP: "+ generatedOtp);
        return generatedOtp;
    }

    public void sendEmail(String subject, String message, String to){

        boolean f = false;

        log.info("Sending mail to: " +to);

        Properties prop = System.getProperties();
        prop.put("mail.smtp.host", SMTP_SERVER);
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.port", "587"); // default port 25
        prop.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(prop, new Authenticator() {
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication(USERNAME, PASSWORD);
            }
        });

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(USERNAME));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            msg.setSubject(subject);
            msg.setText(message);
            Transport.send(msg);
            log.debug("DONE");
        } catch (MessagingException ex) {
            f =  true;
            ex.printStackTrace();
        }
    }

    public User changePassword(String username, User user) throws UsernameNotFoundException{
        User retrievedUser = userRepository.findByUserName(username)
                .orElseThrow(()-> new UsernameNotFoundException("User Name Not Found"));

        retrievedUser.setPassword(passwordEncoder.encode(user.getPassword()));
        final User newUser = userRepository.save(retrievedUser);
        return newUser;
    }
}
