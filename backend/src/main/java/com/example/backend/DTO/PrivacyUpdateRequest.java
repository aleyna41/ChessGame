package com.example.backend.DTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrivacyUpdateRequest {
    private String username;
    private boolean isPrivate;

    public PrivacyUpdateRequest(String username, boolean isPrivate){
        this.username = username;
        this.isPrivate = isPrivate;
    }
}
