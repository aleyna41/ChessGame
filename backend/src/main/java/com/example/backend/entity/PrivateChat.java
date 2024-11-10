package com.example.backend.entity;

import com.example.backend.entity.ChatHistory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
@Getter
@Setter
@Entity
public class PrivateChat extends Chat{

    @Column (nullable = false)
    private String friendName;

    @Column(nullable = false)
    private String creatorName;

    public PrivateChat() {

    }
}
