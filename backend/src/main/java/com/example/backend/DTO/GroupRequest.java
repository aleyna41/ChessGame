package com.example.backend.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupRequest {
    private String groupName;
    private String creator;
    private String[] members;

    public GroupRequest(String groupName, String creator, String[] members){
        this.groupName = groupName;
        this.creator = creator;
        this.members = members;
    }
}
