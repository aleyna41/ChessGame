package com.example.backend.DTO;

import com.example.backend.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FriendListDTO {

    private User sender;
    private User receiver;

    public FriendListDTO(User sender, User receiver){
        this.sender= sender;
        this.receiver= receiver;
    }

}
