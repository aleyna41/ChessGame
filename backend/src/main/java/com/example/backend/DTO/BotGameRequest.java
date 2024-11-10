package com.example.backend.DTO;
import com.example.backend.entity.GameRequestStatus;
import com.example.backend.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BotGameRequest {



    private String gameName;
    private String playerName;
    private int time;
    //private int depth;



    public BotGameRequest(String gameName, String playerName, int time/*, int depth*/){
        this.gameName = gameName;
        this.playerName = playerName;
        this.time = time;
    }

    public BotGameRequest() {}





}