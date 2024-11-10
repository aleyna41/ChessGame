package com.example.backend.service;

import com.example.backend.DTO.ChatInfo;
import com.example.backend.DTO.GroupRequest;
import com.example.backend.DTO.MessageDto;
import com.example.backend.SecurityService;
import com.example.backend.entity.*;
import com.example.backend.exception.ApiRequestException;
import com.example.backend.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ChatService {
    private final UserRepository userRepository;
    private final PrivateChatRepository privateChatRepository;
    private final ChatHistoryRepository chatHistoryRepository;
    private final GroupChatRepository groupChatRepository;
    private final MessageRepository messageRepository;
    private final ClubChatRepository clubChatRepository;
    private final ChessClubRepository chessClubRepository;
    private final FriendService friendService;
    private final SecurityService securityService;

    public ChatService(ChatHistoryRepository chatHistoryRepository,
                       PrivateChatRepository privateChatRepository,
                       UserRepository userRepository,
                       MessageRepository messageRepository,
                       ClubChatRepository clubChatRepository,
                       ChessClubRepository chessClubRepository,
                       GroupChatRepository groupChatRepository,
                       FriendService friendService,
                       SecurityService securityService) {
        this.chatHistoryRepository = chatHistoryRepository;
        this.privateChatRepository = privateChatRepository;
        this.userRepository = userRepository;
        this.groupChatRepository = groupChatRepository;
        this.messageRepository = messageRepository;
        this.clubChatRepository = clubChatRepository;
        this.chessClubRepository = chessClubRepository;
        this.friendService = friendService;
        this.securityService = securityService;
    }

    public PrivateChat createPrivateChat(String creator, String friend) throws ApiRequestException{
        // Wenn User und Friend existieren und sie noch keinen private-chat haben und befreundet sind
        if (securityService.isValidToken(creator) && userRepository.findByUsername(creator)
                != null) {
            if(userRepository.findByUsername(friend) != null && (friendService.areAlreadyFriends(creator,friend) ||
                    friendService.areAlreadyFriends(friend,creator))){
                if (getPrivateChat(creator, friend) == null) {
                    // Chat und Verlauf erstellen
                    PrivateChat privateChat = new PrivateChat();
                    ChatHistory chatHistory = new ChatHistory(null, privateChat);
                    privateChat.setChatHistory(chatHistory);
                    privateChat.setCreatorName(creator);
                    privateChat.setCreationDate(new Date());
                    privateChat.setFriendName(friend);
                    privateChat.setDeleted(false);
                    chatHistory.setChat(privateChat);

                    // System-message erstellen
                    Message message = new Message(chatHistory, creator
                            + " created this private-chat!", "System", MessageType.SYSTEM, true, null);
                    chatHistory.setMessages(Collections.singletonList(message));

                    // Chatverlauf und Chat speichern
                    chatHistoryRepository.save(chatHistory);
                    privateChatRepository.save(privateChat);

                    return privateChat;
                } else {
                    throw new ApiRequestException("You already have a private-chat with "+ friend+"!");
                }
            } else {
                throw new ApiRequestException("User "+friend+" does not exist or is not your friend!");
            }
        } else return null;
    }

    public GroupChat createGroupChat(GroupRequest groupRequest) throws ApiRequestException {
        String name = groupRequest.getGroupName();
        String creator = groupRequest.getCreator();
        String[] members = groupRequest.getMembers();

        if (securityService.isValidToken(creator) && userRepository.findByUsername(creator) != null) {
           if(getGroupChat(name, creator) == null) {
               if(members.length>0) {
                   for (String member : members) {
                       if (getGroupChat(name, member) != null) {
                           throw new ApiRequestException("Please choose another name for your group-chat!");
                       }
                       if (!userRepository.existsByUsername(member)) {
                           throw new ApiRequestException("User " + member + " does not exist!");
                       }
                   }

                   // Chat und Verlauf erstellen
                   GroupChat groupChat = new GroupChat();
                   ChatHistory chatHistory = new ChatHistory(null, groupChat);
                   groupChat.setChatHistory(chatHistory);
                   groupChat.setCreatorName(creator);
                   groupChat.setCreationDate(new Date());
                   groupChat.setGroupName(name);
                   groupChat.setDeleted(false);
                   chatHistory.setChat(groupChat);

                   List<String> groupMembers = new ArrayList<>();
                   groupMembers.add(creator);
                   for (String member : members) {
                       User user = userRepository.findByUsername(member);
                       if (user != null && !groupMembers.contains(member)) {
                           groupMembers.add(member);
                       }
                   }
                   groupChat.setMembers(groupMembers);

                   // System-message erstellen
                   Message message = new Message(chatHistory, creator
                           + " created this group-chat!", "System", MessageType.SYSTEM, true, null);
                   chatHistory.setMessages(Collections.singletonList(message));

                   // Chatverlauf und Chat speichern
                   chatHistoryRepository.save(chatHistory);
                   groupChatRepository.save(groupChat);

                   return groupChat;
               } else {
                   throw new ApiRequestException("You can not have a group-chat with no friends!");
               }
           } else {
               throw new ApiRequestException("Please choose another name for your group-chat!");
           }
        } else {
            throw new ApiRequestException("An error has occurred!");
        }
    }

    public ClubChat createClubChat(String clubname) {
        // Chat und Verlauf erstellen
        ChessClub club = chessClubRepository.findByClubName(clubname);
        ClubChat clubChat = new ClubChat(club);
        ChatHistory chatHistory = new ChatHistory(null, clubChat);
        clubChat.setChatHistory(chatHistory);
        clubChat.setCreationDate(new Date());
        clubChat.setDeleted(false);
        chatHistory.setChat(clubChat);
        // System-message erstellen
        Message message = new Message(chatHistory, "This is the " + clubname + " club-chat!",
                "System", MessageType.SYSTEM, true, null);
        chatHistory.setMessages(Collections.singletonList(message));
        // Chatverlauf und Chat speichern
        chatHistoryRepository.save(chatHistory);
        clubChatRepository.save(clubChat);
        return clubChat;
    }

    public PrivateChat getPrivateChat(String creator, String friend) {
        return privateChatRepository.getPrivateChat(creator, friend);
    }

    public void createMessage(MessageDto messageDto, Chat chat) {
        Message message = new Message();
        message.setSender(messageDto.getSender());
        message.setContent(messageDto.getContent());
        message.setType(convertToMessageType(messageDto.getType()));
        message.setTimestamp(LocalDateTime.now());
        message.setSeen(messageDto.getSeen());
        message.setChatHistory(chat.getChatHistory());

        ChatHistory chatHistory = chat.getChatHistory();
        message.setChatHistory(chatHistory);
        chatHistory.getMessages().add(message);
        chatHistoryRepository.save(chatHistory);
        messageRepository.save(message);
    }

    public static MessageType convertToMessageType(String messageType) {
        return switch (messageType.toUpperCase()) {
            case "CHAT" -> MessageType.CHAT;
            case "SYSTEM" -> MessageType.SYSTEM;
            default -> throw new IllegalArgumentException("Ungültiger MessageType: " + messageType);
        };
    }

    public boolean sendClubMessage(MessageDto message) {
        String club = message.getReceiver();
        ClubChat chat = clubChatRepository.getClubChat(club);
        if (chat != null) {
            createMessage(message, chat);
            return true;
        } else return false;
    }

    public boolean sendGroupMessage(MessageDto message) {
        String group = message.getReceiver();
        GroupChat chat = groupChatRepository.getGroupChat2(group, message.getSender());

        if (chat != null && !chat.getDeleted()) {
            createMessage(message, chat);
            return true;
        } else return false;
    }

    public boolean createAndSendPrivateMessage(MessageDto messageDto) {
        String user1 = messageDto.getReceiver();
        String user2 = messageDto.getSender();
        if (privateChatRepository.getPrivateChat(user1, user2) != null &&
                !privateChatRepository.getPrivateChat(user1, user2).getDeleted()) {
            PrivateChat privateChat = privateChatRepository.getPrivateChat(user1, user2);
            createMessage(messageDto, privateChat);
            return true;
        } else if (privateChatRepository.getPrivateChat(user2, user1) != null &&
                !privateChatRepository.getPrivateChat(user2, user1).getDeleted()) {
            PrivateChat privateChat = privateChatRepository.getPrivateChat(user2, user1);
            createMessage(messageDto, privateChat);
            return true;
        } else return false;
    }

    public List<Message> getPrivateChatHistory(String creator, String chatName) {
        if (privateChatRepository.getPrivateChat(creator, chatName) != null) {
            return chatHistoryRepository.getChatHistory(privateChatRepository.getPrivateChat(creator, chatName)).getMessages();
        } else return null;
    }

    public List<Message> getClubChatHistory(String clubName) {
        if (clubChatRepository.getClubChat(clubName) != null) {
            return chatHistoryRepository.getClubHistory(clubChatRepository.getClubChat(clubName)).getMessages();
        } else return null;
    }

    public List<Message> getGroupChatHistory(String username, String groupName) {
        if (groupChatRepository.getGroupChat2(groupName, username) != null) {
            return chatHistoryRepository.getGroupHistory(groupChatRepository
                    .getGroupChat2(groupName, username)).getMessages();
        } else return null;
    }

    public List<ChatInfo> getPrivateChats(String username) {
        List<PrivateChat> privateChats = privateChatRepository.getPrivateChats(username);
        List<ChatInfo> chatinfos = new ArrayList<>();
        for (PrivateChat chat : privateChats) {
            if(chat.getDeleter() == null || !chat.getDeleter().equals(username)) {
                if (!username.equals(chat.getCreatorName())) {
                    chatinfos.add(new ChatInfo(chat.getCreatorName(), chat.getCreationDate(), null));
                } else {
                    chatinfos.add(new ChatInfo(chat.getFriendName(), chat.getCreationDate(), null));
                }
            }
        }
        return chatinfos;
    }

    public List<ChatInfo> getGroupChats(String username) {
        List<GroupChat> groupChats = groupChatRepository.getGroupChats(username);
        List<ChatInfo> chatinfos = new ArrayList<>();
        for (GroupChat chat : groupChats) {
            chatinfos.add(new ChatInfo(chat.getGroupName(), chat.getCreationDate(), chat.getMembers()));
        }
        return chatinfos;
    }

    public GroupChat getGroupChat(String creator, String name) {
        return groupChatRepository.getGroupChat(name, creator);
    }

    public boolean deletePrivateChat(String username, String friendName) {
        PrivateChat chat1 = privateChatRepository.getPrivateChat(username,friendName);
        PrivateChat chat2 = privateChatRepository.getPrivateChat(friendName,username);
        // Die Freundschaft wurde beendet, der Chatverlauf bleibt, bis er gelöscht wird
        if (!friendService.areAlreadyFriends(username, friendName) &&
                !friendService.areAlreadyFriends(friendName, username)){
            setDeleted(chat1, chat2);
            return true;
        }

        // Der Freund hat den Chat bereits gelöscht und wird endgültig mit Verlauf gelöscht
        else if (chat1 != null && chat1.getDeleted() || chat2 != null && chat2.getDeleted()) {
            deleteChat(chat1, chat2);
            return true;
        }

        // Keiner hat den Chat bisher gelöscht
        else if (chat1 != null && !chat1.getDeleted() || chat2 != null && !chat2.getDeleted()) {
            PrivateChat chat = setDeleted(chat1, chat2);
            chat.setDeleter(username);
            // Systemnachricht an den Freund
            List<Message> messages = chat.getChatHistory().getMessages();
            Message message = new Message(chat.getChatHistory(), "Your friend " + username + " deleted this chat!",
                    "System", MessageType.SYSTEM, true, null);
            Message message2 = new Message(chat.getChatHistory(), "You can see the chathistory until you delete this hat too.",
                    "System", MessageType.SYSTEM, true, null);
            messages.add(message);
            messages.add(message2);
            ChatHistory history = chat.getChatHistory();
            history.setMessages(messages);
            chat.setChatHistory(history);
            chatHistoryRepository.save(history);
            privateChatRepository.save(chat);
            return true;
        } else return false;
    }

    public PrivateChat setDeleted(PrivateChat chat1, PrivateChat chat2){
        if(chat1==null){
            chat2.setDeleted(true);
            privateChatRepository.save(chat2);
            return chat2;
        } else {
            chat1.setDeleted(true);
            privateChatRepository.save(chat1);
            return chat1;
        }
    }

    public void deleteChat(PrivateChat chat1, PrivateChat chat2){
        if(chat1==null){
            chatHistoryRepository.delete(chat2.getChatHistory());
            privateChatRepository.delete(chat2);
        } else {
            chatHistoryRepository.delete(chat1.getChatHistory());
            privateChatRepository.delete(chat1);
        }
    }

    public boolean leaveGroupChat(String username, String chatname) {
        GroupChat chat = groupChatRepository.getGroupChat2(chatname, username);
        if(chat!=null) {
            List<String> members = chat.getMembers();
            if (members.isEmpty()) {
                chatHistoryRepository.delete(chat.getChatHistory());
                groupChatRepository.delete(chat);
                return true;
            }
            members.remove(username);
            if (members.size() < 2) {
                chat.setDeleted(true);
                // Systemnachricht an den letzten Member
                List<Message> messages = chat.getChatHistory().getMessages();
                Message message = new Message(chat.getChatHistory(), username + " left this group-chat!",
                        "System", MessageType.SYSTEM, true, null);
                Message message1 = new Message(chat.getChatHistory(), "You are the only member of this group-chat!",
                        "System", MessageType.SYSTEM, true, null);
                Message message2 = new Message(chat.getChatHistory(), "You can see the chathistory until you delete this chat too.",
                        "System", MessageType.SYSTEM, true, null);
                messages.add(message);
                messages.add(message1);
                messages.add(message2);
                ChatHistory history = chat.getChatHistory();
                history.setMessages(messages);
                chatHistoryRepository.save(history);
                return true;
            } else {
                chat.setMembers(members);

                // Systemnachricht an die anderen Members
                List<Message> messages = chat.getChatHistory().getMessages();
                Message message = new Message(chat.getChatHistory(), username + " left this group-chat!",
                        "System", MessageType.SYSTEM, true, null);
                messages.add(message);
                ChatHistory history = chat.getChatHistory();
                history.setMessages(messages);
                chatHistoryRepository.save(history);

                groupChatRepository.save(chat);
                return true;
            }
        } else return false;
    }

    public boolean markAsSeen(String loggedUser, Long msgId) {
        Message message = messageRepository.findByMsgId(msgId);
        if (loggedUser.equals(message.getSender())) {
            return false;
        } else {
            message.setSeen(true);
            messageRepository.save(message);
            return true;
        }
    }

    public boolean deleteMessage(Long msgId) {
        Message message = messageRepository.findByMsgId(msgId);
        if(!message.getSeen()){
            messageRepository.delete(message);
            return true;
        } else return false;
    }

    public boolean editMessage(Long msgId, String newContent) {
        Message message = messageRepository.findByMsgId(msgId);
        if(!message.getSeen()){
            message.setContent(newContent);
            messageRepository.save(message);
            return true;
        } else return false;
    }
}
