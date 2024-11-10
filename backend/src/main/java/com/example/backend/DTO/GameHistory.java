package com.example.backend.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class GameHistory {

    private String user;
    private String enemy;
    private int enemyPoints;
    private int userPoints;
    private String result;
    private Date date;
    private Long gameId;
    private String white;
    private String black;

    public GameHistory(String user, String enemy, int enemyPoints, int userPoints, String result, Date date, Long gameId,
                       String white, String black){
    this.user = user;
    this.enemy = enemy;
    this.enemyPoints = enemyPoints;
    this.userPoints = userPoints;
    this.result = result;
    this.date = date;
    this.gameId = gameId;
    this.white = white;
    this.black = black;
    }
}