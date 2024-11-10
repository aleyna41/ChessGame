package com.example.backend.DTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class DrawRequest {


    @Id
    private Long id;

    @Column
    private String gameName;

    @Column
    private String senderUserName;

    @Column
    private String receiverUserName;

    public DrawRequest(String gameName, String receiverUserName, String senderUserName){
        this.gameName = gameName;
        this.receiverUserName = receiverUserName;
        this.senderUserName = senderUserName;
    }

    public DrawRequest(){}
}
