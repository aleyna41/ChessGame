package com.example.backend.entity;
import ch.qos.logback.core.util.COWArrayList;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.example.backend.entity.User;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Getter
@Setter
@Entity
public class ChessClub {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Column(name = "id")
    private Long clubId;

    private String clubName;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> members;


    public ChessClub(String clubName, List<String>members) { //?
        this.clubName = clubName;
        this.members = members;
    }

    /*public ChessClub(String clubName) {
        this.clubName = clubName;
        this.members = new ArrayList<>();
    }*/

    public ChessClub() {
    }


    //-----------TEST--------------------

    @Override
    public String toString() {
        return "ChessClub{" + "id=" + id + ", clubName='" + clubName + '\'' + ", members=" + members + '}';
    }
}





