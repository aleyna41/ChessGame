package com.example.backend.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatId;

    @OneToOne(mappedBy = "chat")
    private ChatHistory chatHistory;

    @Column(nullable = false)
    private Date creationDate;

    @Column
    private boolean deleted;

    @Column
    private String deleter;

    public boolean getDeleted(){
        return this.deleted;
    }
}
