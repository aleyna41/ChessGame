package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Entity
@Getter
@Setter
public class GroupChat extends Chat{

    @Column(nullable = false, unique = true)
    private String groupName;

    @Column(nullable = false)
    private String creatorName;

    @ElementCollection
    private List<String> members;

    public GroupChat(String groupName, String creatorName, List<String> members, ChatHistory chatHistory) {
        this.groupName = groupName;
        this.creatorName = creatorName;
        this.members = members;
    }

    public GroupChat() {
    }
}
