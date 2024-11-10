package com.example.backend.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class ChatInfo {

    private String friendName;
    private Date creationDate;
    private List<String> memberUsernames;

    public ChatInfo(String friendName, Date creationDate, List<String> memberUsernames){
        this.friendName= friendName;
        this.creationDate = creationDate;
        this.memberUsernames = memberUsernames;
    }

}
