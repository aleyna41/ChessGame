package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
@Entity
public class SessionToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long tokenId;
   // @Column(nullable = false)
   // private Date expirationDate;
    @Column(nullable = false)
    private String username;

    public SessionToken(/*Date expirationDate,*/ String username) {
        //this.expirationDate = expirationDate;
        this.username = username;
    }

    public SessionToken() {

    }
}