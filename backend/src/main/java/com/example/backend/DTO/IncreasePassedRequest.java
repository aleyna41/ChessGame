package com.example.backend.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IncreasePassedRequest {
    private String username;
    private String puzzleId;

    public IncreasePassedRequest(String username, String puzzleId){
        this.username = username;
        this.puzzleId = puzzleId;
    }



}