package com.example.backend.DTO;
import com.example.backend.entity.GameRequestStatus;
import com.example.backend.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class GameRequest {
    // Vorläufig damit keine Errors in User Klassen
    @Id
    private long gameid;
    @Column
    private String gameName;
    @Column
    private String player1;
    @Column
    private String player2;

    private int time;

    @Enumerated(EnumType.STRING)
    private GameRequestStatus status;

    public  GameRequest( String gameName, String player1, String player2, int time){
        this.gameName = gameName;
        this.player1 = player1;
        this.player2 = player2;
        this.time = time;
    }

    public GameRequest() {
    }
}