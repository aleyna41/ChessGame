package com.example.backend.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
//import com.example.backend.DTO.GameRequest;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class User {
    // Attribute
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long userID;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String birthDate;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private int points;
    @Column(nullable = false)
    private String encodedPassword;
    @Column(nullable = false)
    private String salt;
    @Column
    private String profilePicture;
    @Column(nullable = false)
    private boolean isPrivate;
    @Column(nullable = true)
    private String anerkennung = "";
    @Column(nullable = false)
    private int passedPuzzle = 0;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "passed_list", joinColumns = @JoinColumn(name = "username"))
    private List<String> passedList = new ArrayList<>();
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "pgn_list", joinColumns = @JoinColumn(name = "username"))
    private List<String> pgnList = new ArrayList<>();


    /*@ManyToOne
    @JoinColumn(name = "chess_club_id")
    private ChessClub chessClub;*/
    //  @OneToMany(fetch = FetchType.EAGER)
    //  private List<GameRequest> gameRequests;

    public User(String username, String firstName, String lastName, String birthDate, String email, String encodedPassword, String salt) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.email = email;
        this.points = 500;
        this.encodedPassword = encodedPassword;
        this.isPrivate = false;
        this.profilePicture = "profilePictures/defaultprofilepicture.png";
        this.salt = salt;
    }

    public boolean getIsPrivate() {
        return isPrivate;
    }

    public void checkAnerkennung() {
        if (passedPuzzle >= 3 && !this.anerkennung.equals("Schachexperte")) {
            this.setAnerkennung("Chessexpert");
        }
    }

    public User() {
        // Muss vorhanden sein, weil von JPA verwendet, um Entitäten zu instanziieren
    }
//    public boolean accept(GameRequest gameRequest){
//           return true;
//           }
//    public boolean decline (GameRequest gameRequest){
//           return false;
//           }
/*
    @Override
    public String toString() {
        return "User{" +
                ", firstName=' " + firstName + '\'' +
                ", lastName=' " + lastName + '\'' +
                ", email=' " + email + '\'' +
                ", profilePicture=' " + Arrays.toString(profilePicture) +
                '}';
    }

 */
}