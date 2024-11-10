package com.example.backend.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyTwoFactorRequest {
    public VerifyTwoFactorRequest(String username, String inputCode){
        this.username = username;
        this.inputCode = inputCode;
    }
    private String username;
    private String inputCode;
}
