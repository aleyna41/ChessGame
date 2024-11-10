package com.example.backend.DTO;

import com.example.backend.entity.ChessClub;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class UserProfile {
    private Long userID;
    private String username;
    private String firstName;
    private String lastName;
    private String birthDate;
    private String email;
    private int points;
    private String profilePicture;
    //private List<String> chessClubs;


    public UserProfile(long userID, String username, String firstName, String lastName, String birthDate,
                       String email, int points, String profilePicture /*,List<String> chessClubs*/) {
        this.userID = userID;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.email = email;
        this.points = points;
        this.profilePicture = profilePicture;
        //this.chessClubs = chessClubs;
    }
}
