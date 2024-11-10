package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ChessPuzzle {

    @Id
    private String puzzleId;
    @Column
    private String fen;
    @Column
    private String moves;
    @Column
    private String themes;
    @Column
    private String gameURL;
    @Column
    private String openingTags;


    public ChessPuzzle(){

    }

    @Override
    public String toString(){
        return "Customer [puzzleId=" + puzzleId + ", fen=" + fen + ", moves=" + moves + ", themes=" + themes + ", gameURL=" + gameURL + ", openingTags=" + openingTags + "]";
    }


}
