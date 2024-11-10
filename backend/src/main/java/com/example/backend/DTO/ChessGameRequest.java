package com.example.backend.DTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ChessGameRequest {

    @Id
    private long gameid;
    @Column
    private String gameName;
    @Column
    private String player1;
    @Column
    private String player2;
    @Column
    private String timer;
    @Column
    private String fenString;
    @Column
    private String sanString;



    public ChessGameRequest(String gameName, String player1, String sanString, String fenString) {
        this.gameName = gameName;
        this.sanString = sanString;
        this.fenString = fenString;
    }

    public ChessGameRequest() {

    }
}