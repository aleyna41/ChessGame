package com.example.backend.entity;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String gameName;
    private String player1;
    private String player2;

    @Enumerated(EnumType.STRING)
    private GameRequestStatus status;

    private boolean gameStarted;

    @ElementCollection
    private List<String> fenString;

    @ElementCollection
    private List<String> sanString;

    private String result;
    private Date completionDate;
    private int time;

    private boolean streaming;

    @Enumerated(EnumType.STRING)
    private DrawRequestStatus drawRequestStatus;



    public Game(String gameName, String player1, String player2, List<String> fenString, List<String> sanString, boolean gameStarted, int time){
        this.gameName = gameName;
        this.player1 = player1;
        this.player2 = player2;
        this.sanString = sanString;
        this.fenString = fenString;
        this.gameStarted = false;
        this.time = time;
    }


    public Game() {}



    //für ausgabe in test:
    @Override
    public String toString() {
        return "Games{" + "id =" + id + ", gameName ='" + gameName + '\'' + ", players =" + player1 + ", "+ player2
                + '}' +", fenString= " + fenString + ", sanString= " + sanString + ", gameStarted= " + gameStarted;
    }


}