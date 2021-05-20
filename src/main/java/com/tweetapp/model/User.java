package com.tweetapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@ApiModel(description = "All Details of TweetApp user")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "User")
@Getter
@Setter
public class User {
    @Transient
    public static final String SEQUENCE_NAME = "user_sequence";

    @Id
    private int id;

    @ApiModelProperty(notes = "Must contain at least 2 characters.")
    private String firstName;

    @ApiModelProperty(notes = "Required")
    private String lastName;

    @ApiModelProperty(notes = "Must contain at least 4 characters.")
    private String userName;

    private String emailId;
    private String contactNumber;
    private String password;
    private boolean confirmed;
    //private boolean active;
    private String roles;

    public User(User user) {
        this.id = user.getId();
        this.confirmed =  user.isConfirmed();
        //this.active = user.isActive();
        this.userName = user.getUserName();
        this.emailId = user.getEmailId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.contactNumber = user.getContactNumber();
    }
}
