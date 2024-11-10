package com.example.backend.DTO;

import com.example.backend.entity.SessionToken;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {
    public UserRequest(String username, SessionToken sessionToken){
        this.username = username;
        this.sessionToken = sessionToken;
    }
    private String username;
    private SessionToken sessionToken;
}
