package com.example.backend.DTO;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class UserLoginRequest {
    public UserLoginRequest(String username, String password){
        this.username = username;
        this.password = password;
    }
    private String username;
    private String password;
   }
