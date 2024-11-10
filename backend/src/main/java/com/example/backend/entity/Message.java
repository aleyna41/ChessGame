package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long msgId;
    @ManyToOne
    @JoinColumn(name = "chat_history_id")
    private ChatHistory chatHistory;
    @Column
    private String content;
    @Column
    private String sender;
    @Column
    private String receiver;
    @Column
    private MessageType type;
    @Column
    private Boolean seen;
    @Column
    private LocalDateTime timestamp;

    public Message(ChatHistory chatHistory, String content,
                   String sender, MessageType type, Boolean seen, LocalDateTime timestamp){
        this.chatHistory = chatHistory;
        this.content = content;
        this.sender = sender;
        this.type = type;
        this.timestamp = timestamp;
        this.seen = seen;
    }

    public Message() {

    }
}
