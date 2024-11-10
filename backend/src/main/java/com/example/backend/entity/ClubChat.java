package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Entity
@Getter
@Setter
public class ClubChat extends Chat {

    @OneToOne
    @JoinColumn(name = "clubId")
    private ChessClub chessClub;

    @Transient
    private String chatName;

    @Transient
    private List<String> members;

    public ClubChat(ChessClub chessClub) {
        this.chessClub = chessClub;
        this.chatName = chessClub.getClubName();
        this.members = chessClub.getMembers();
    }

    public ClubChat() {

    }
}