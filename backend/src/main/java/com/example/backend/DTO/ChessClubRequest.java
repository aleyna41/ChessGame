package com.example.backend.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChessClubRequest {
    private String member;
    private String clubName;

    public ChessClubRequest(String member, String clubName){
        this.member = member;
        this.clubName = clubName;
    }



}
