package com.example.backend.DTO;

import com.example.backend.entity.MessageType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageDto {
    private String content;
    private String sender;
    private String receiver;
    private String type;
    private Boolean seen;
    private Long msgId;
    //private String timestamp;

    public MessageDto(String content, String sender, String receiver, String type, Boolean seen, Long msgId /*String timestamp*/){
       this.content = content;
       this.sender = sender;
       this.receiver = receiver;
       this.type = type;
       this.seen = seen;
       this.msgId = msgId;
       //this.timestamp = timestamp;
    }
}
