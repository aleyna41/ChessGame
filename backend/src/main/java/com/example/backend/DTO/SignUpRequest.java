package com.example.backend.DTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
public class SignUpRequest {
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private String email;
    private String birthdate;
    //private MultipartFile profilePicture;

    public SignUpRequest(String username, String password, String firstname, String lastname, String email, String birthdate) {
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.birthdate = birthdate;
        //this.profilePicture = profilePicture;
    }

}