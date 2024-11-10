package com.example.backend.controller;
import com.example.backend.DTO.GroupRequest;
import com.example.backend.entity.*;
import com.example.backend.exception.ApiRequestException;
import com.example.backend.DTO.ChatInfo;
import com.example.backend.DTO.MessageDto;
import com.example.backend.service.ChatService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
public class ChatController {

    private final ChatService chatService;
    public ChatController(ChatService chatService){
        this.chatService = chatService;
    }

    // Anfrage zur Erstellung eines Privaten Chats mit einem Freund
    @PostMapping("/privateChat")
    public ResponseEntity<ChatInfo> createPrivateChat(@RequestBody Map<String, String> chatRequest) throws ApiRequestException {
        String creatorName = chatRequest.get("creatorName");
        String friendName = chatRequest.get("friendName");
        // Private-chat erstellen
        PrivateChat privateChat = chatService.createPrivateChat(creatorName, friendName);
        // Chat-Info erstellen und an frontend geben
        if (privateChat != null) {
            ChatInfo chatInfo = new ChatInfo(privateChat.getFriendName(), privateChat.getCreationDate(), null);
            return ResponseEntity.ok(chatInfo);
        } else return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).build();
    }

    // Anfrage zur Erstellung eines Gruppen-Chats
    @PostMapping("/groupChat")
    public ResponseEntity<ChatInfo> createGroupChat(@RequestBody GroupRequest groupRequest) throws ApiRequestException {
        System.out.println("createGroupchat aufgerufen " + groupRequest.getCreator() + groupRequest.getGroupName());
        GroupChat groupChat = chatService.createGroupChat(groupRequest);
        if (groupChat != null) {
            ChatInfo chatInfo = new ChatInfo(groupChat.getCreatorName(), groupChat.getCreationDate(), groupChat.getMembers());
            return ResponseEntity.ok(chatInfo);
        } else return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).build();
    }

    // Anfrage zur Erstellung eines Club-Chats
    @PostMapping("/clubChat")
    public ResponseEntity<ChatInfo> createClubChat(@RequestBody String club){
        ClubChat clubChat = chatService.createClubChat(club);
        if (clubChat != null) {
            return ResponseEntity.ok().build();
        } else return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).build();
    }

    // Anfrage zum Erstellen und Speichern einer Nachricht im Privat-Chat
    @PostMapping("/sendMessage")
    public ResponseEntity<ChatInfo> sendMessage(@RequestBody MessageDto message) {
        if (chatService.createAndSendPrivateMessage(message)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).build();
        }
    }

    // Anfrage zum Erstellen und Speichern einer Nachricht im Club-Chat
    @PostMapping("/sendClubMessage")
    public ResponseEntity<ChatInfo> sendClubMessage(@RequestBody MessageDto message) {
        if (chatService.sendClubMessage(message)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).build();
        }
    }

    // Anfrage zum Erstellen und Speichern einer Nachricht im Gruppen-Chat
    @PostMapping("/sendGroupMessage")
    public ResponseEntity<ChatInfo> sendGroupMessage(@RequestBody MessageDto message) {
        System.out.println("Controller: "+message.getReceiver());
        if (chatService.sendGroupMessage(message)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).build();
        }
    }

    // Übergeben des Chat-Verlaufs für alle Chat-Typen
    @GetMapping("/getChatHistory/{username}/{friendName}")
    public ResponseEntity<List<MessageDto>> getChatHistory(@PathVariable String username, @PathVariable String friendName,
                                                                  @RequestParam String chatType) {
        username = username.replaceAll("\"", "");
        friendName = friendName.replaceAll("\"", "");

        // Private-Chat-Verlauf
        if(chatType.equals("private")) {
            if (chatService.getPrivateChatHistory(username, friendName) == null &&
                    chatService.getPrivateChatHistory(friendName, username) == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            } else if (chatService.getPrivateChatHistory(username, friendName) != null) {
                return returnPrivateMessages(friendName, username);
            } else {
                return returnPrivateMessages(username, friendName);
            }
        }

        // Club-Chat-Verlauf
        else if(chatType.equals("club")&&chatService.getClubChatHistory(friendName)!=null){
            List<Message> messages2 = chatService.getClubChatHistory(friendName);
            if (messages2 == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            } else {
                List<MessageDto> messageDtoList = messages2.stream()
                        .map(message -> new MessageDto(message.getContent(), message.getSender(), message.getReceiver(),
                                message.getType().toString(), message.getSeen(), message.getMsgId()))
                        .collect(Collectors.toList());

                return ResponseEntity.ok(messageDtoList);
            }
        }

        // Group-Chat-Verlauf
        else if(chatType.equals(("group"))&&chatService.getGroupChatHistory(username, friendName)!= null){
            List<Message> messages2 = chatService.getGroupChatHistory(username, friendName);
            if (messages2 == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            } else {
                List<MessageDto> messageDtoList = messages2.stream()
                        .map(message -> new MessageDto(message.getContent(), message.getSender(), message.getReceiver(),
                                message.getType().toString(), message.getSeen(), message.getMsgId()))
                        .collect(Collectors.toList());

                return ResponseEntity.ok(messageDtoList);
            }
        } else return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    // In getChatHistory für Privat-Chat-Verlauf verwendet
    private ResponseEntity<List<MessageDto>> returnPrivateMessages(@PathVariable String username, @PathVariable String friendName) {
        List<Message> messages2 = chatService.getPrivateChatHistory(friendName, username);
        if (messages2 == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            List<MessageDto> messageDtoList = messages2.stream()
                    .map(message -> new MessageDto(message.getContent(), message.getSender(), message.getReceiver(),
                            message.getType().toString(),message.getSeen(), message.getMsgId()))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(messageDtoList);
        }
    }

    // Anfrage zum Setzen einer Nachricht auf 'seen'
    @PostMapping("/seen/{loggedUser}")
    public ResponseEntity<Void> seen(@PathVariable String loggedUser, @RequestBody MessageDto message) {
        loggedUser = loggedUser.replaceAll("\"", "");
        Long msgId = message.getMsgId();
        System.out.println(msgId);
        if (chatService.markAsSeen(loggedUser, message.getMsgId())) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Anfrage zum Bearbeiten einer Nachricht
    @PostMapping("/editMessage/{editingMessage}/{msgId}")
    public ResponseEntity<Void> editMessage(@PathVariable String editingMessage, @PathVariable Long msgId){
        System.out.println(msgId + " edit");
        if (chatService.editMessage(msgId, editingMessage)){
            System.out.println("success");
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Anfrage zum Löschen einer Nachricht
    @PostMapping("/deleteMessage")
    public ResponseEntity<Void> deleteMessage(@RequestBody MessageDto origMsg){
        if (chatService.deleteMessage(origMsg.getMsgId())){
            return ResponseEntity.ok().build();
        } else {
         return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Alle Privat-Chats eines Users übergeben
    @GetMapping("/getPrivateChats/{username}")
    public ResponseEntity<List<ChatInfo>> getPrivateChats(@PathVariable String username) throws ApiRequestException {
        username = username.replaceAll("\"", "");
        if (!chatService.getPrivateChats(username).isEmpty()) {
            return ResponseEntity.ok(chatService.getPrivateChats(username));
        } else return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // Alle Gruppen-Chats eines Users übergeben
    @GetMapping("/getGroupChats/{username}")
    public ResponseEntity<List<ChatInfo>> getGroupChats(@PathVariable String username) throws ApiRequestException {
        username = username.replaceAll("\"", "");
        if (!chatService.getGroupChats(username).isEmpty()) {
            return ResponseEntity.ok(chatService.getGroupChats(username));
        } else return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // Einen Chat löschen
    @PostMapping("/deleteChat")
    public ResponseEntity<Void> deleteChat(@RequestBody String jsonBody) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonBody);
            String loggedUser = jsonNode.get("loggedUser").asText();
            String friendName = jsonNode.get("friendName").asText();
            String chatType = jsonNode.get("chatType").asText();
        System.out.println(loggedUser+" "+friendName+" "+chatType + " ");
        if (chatType.equals("private") && chatService.deletePrivateChat(loggedUser, friendName)){
            return ResponseEntity.ok().build();
        } if (chatType.equals("group") && chatService.leaveGroupChat(loggedUser, friendName)){
            return ResponseEntity.ok().build();
        } else return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).build();
        }
    }

}