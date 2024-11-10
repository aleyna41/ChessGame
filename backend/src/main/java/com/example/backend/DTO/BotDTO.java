package com.example.backend.DTO;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class BotDTO {
    @Id
    private Long id;

    private String fen;
    private int depth;


    public BotDTO(String fen, int depth){
        this.fen = fen;
        this.depth = depth;
    }

    public BotDTO(){}


}
