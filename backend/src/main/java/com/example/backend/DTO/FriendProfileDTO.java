package com.example.backend.DTO;

import com.example.backend.entity.SessionToken;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FriendProfileDTO {


    private String username;
    private String firstName;
    private String lastName;
    private int points;
    private String profilePicture;
    private SessionToken sessionToken;


    public FriendProfileDTO(String username, String firstName, String lastName, int points,String profilePicture) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.points = points;
        this.profilePicture = profilePicture;

    }


}
