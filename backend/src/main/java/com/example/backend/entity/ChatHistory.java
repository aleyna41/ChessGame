package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
// später
// zum speichern der nachrichten in einem chat ? oder lieber message repository ?
@Getter
@Setter
@Entity
public class ChatHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historyId;
    @OneToMany(mappedBy = "chatHistory", cascade = CascadeType.ALL)
    private List<Message> messages;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "chatId")
    private Chat chat;

    public ChatHistory (List<Message> messages, Chat chat){
        this.messages = messages;
        this.chat = chat;
    }

    public ChatHistory() {

    }
}
