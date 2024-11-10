package com.example.backend.DTO;
import com.example.backend.entity.SessionToken;
import com.example.backend.entity.User;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
public class FriendRequestDTO {

    private User sender;
    private User receiver;
    private SessionToken sessionToken;
    private boolean isPrivate;



    public FriendRequestDTO(User sender, User receiver, SessionToken sessionToken, boolean isPrivate){
        this.sender= sender;
        this.receiver= receiver;
        this.sessionToken= sessionToken;
        this.isPrivate= isPrivate;

    }
    public boolean getIsPrivate(){
        return isPrivate;
    }






}

