package com.example.backend.DTO;

import com.example.backend.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LeaderboardDTO {
    private String username;
    private int points;


    public LeaderboardDTO(String username, int points) {
        this.username = username;
        this.points = points;
    }



}
