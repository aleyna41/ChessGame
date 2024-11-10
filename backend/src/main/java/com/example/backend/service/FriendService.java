package com.example.backend.service;
import com.example.backend.DTO.FriendProfileDTO;
import com.example.backend.DTO.FriendRequestDTO;
import com.example.backend.DTO.UserProfile;
import com.example.backend.DTO.UserRequest;
import com.example.backend.SecurityService;
import com.example.backend.entity.Friend;
import com.example.backend.entity.FriendStatus;
import com.example.backend.entity.User;
import com.example.backend.exception.ApiRequestException;
import com.example.backend.repository.FriendRepository;
import com.example.backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

@Service
public class FriendService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;
    private final SecurityService securityService;
    private final UserService userService;


    public FriendService(FriendRepository friendRepository, UserRepository userRepository, SecurityService securityService, UserService userService) {
        this.friendRepository = friendRepository;
        this.userRepository = userRepository;
        this.securityService = securityService;
        this.userService = userService;
    }

    public boolean areAlreadyFriends(String sender, String receiver) {
        if (friendRepository.getFriendInvitation(sender, receiver) != null) {
            return true;
        }
        else
            return false;
    }

    public void sendFriendRequest(String senderUsername, String receiverUsername) throws ApiRequestException {
        User sender = userRepository.findByUsername(senderUsername);
        User receiver = userRepository.findByUsername(receiverUsername);

        if (sender == null || receiver == null) {
            throw new ApiRequestException("Sender or receiver not found");
        }
        else if (areAlreadyFriends(senderUsername, receiverUsername)) {
            System.out.println("schon befreundet");
            throw new ApiRequestException("Die User sind schon befreundet");

        } else {
            System.out.println("super!");
            Friend friendInvitation = new Friend(sender.getUsername(), receiver.getUsername());

            friendInvitation.setStatus(FriendStatus.PENDING);
            friendRepository.save(friendInvitation);
            friendInvitation.sendFriendRequestNotif(receiver);
        }
    }


    public void acceptFriendRequest(String senderName, String receiverName) throws ApiRequestException {
        System.out.println(senderName);
        System.out.println(receiverName);
        try {

            Friend friendInviation = friendRepository.getFriendInvitation(senderName, receiverName);
            friendInviation.setStatus(FriendStatus.ACCEPTED);
            friendRepository.save(friendInviation);

        } catch (Exception e) {
            throw new ApiRequestException("Anfrage kann nicht angenommen werden");
        }
    }


    public void rejectFriendRequest(String senderName, String receiverName) throws ApiRequestException {
        try {

            Friend friendInviation = friendRepository.getFriendInvitation(senderName, receiverName);

            System.out.println("friend:" + friendInviation);
            if(friendInviation.getStatus() == FriendStatus.PENDING){
                friendInviation.setStatus(FriendStatus.REJECTED);
                friendRepository.delete(friendInviation);
            }
        } catch (Exception e) {
            //EXCEPTION WERFEN
            throw new ApiRequestException("Anfrage kann nicht angenommen werden");
        }
    }



    public void removeFriend(String senderName, String receiverName) throws ApiRequestException {
        try {
            Friend friendInviation = friendRepository.getFriendInvitation(senderName, receiverName);

            System.out.println("gelöschte freundschaf"+ friendInviation);
            if(friendInviation.getStatus() == FriendStatus.ACCEPTED){
                //friendInviation.setStatus(FriendStatus.REJECTED);
                friendRepository.delete(friendInviation);

            }
        } catch (Exception e) {
            throw new ApiRequestException("Akzeptierte Freundesanfragen können nicht entfernt werden");
        }
    }



    public List<String> getFriends(String userName) throws ApiRequestException {

        List<Friend> friends = friendRepository.getFriends(userName, FriendStatus.ACCEPTED);
        List<String> friendNames = new ArrayList<>();
        for (Friend friend : friends) {
            if (!userName.equals(friend.getReceiver())){
                friendNames.add(friend.getReceiver());
            } else {
                friendNames.add(friend.getSender());
            }
        }
        return friendNames;
    }

    public List<String> getPendingFriendList(String userName) throws ApiRequestException {
        List<Friend> friends = friendRepository.getPendingFriendList(userName, FriendStatus.PENDING);
        List<String> friendNames = new ArrayList<>();
        for (Friend friend : friends) {
            if (userName.equals(friend.getReceiver())){
                friendNames.add(friend.getSender());


            }        }

        return friendNames;

    }


    public List<Friend> getFriendList() {
        return friendRepository.findAll();
    }
}

/*public FriendProfileDTO getFriendProfile(String username) {
        // Wenn der Token gültig ist werden die Profildaten übergeben
            if (securityService.isValidToken(username)){
                User user = userRepository.findByUsername(username);
                if(!user.getIsPrivate()) {
                System.out.println("GetFriendProfile geht");
                return new FriendProfileDTO(user.getUsername(), user.getFirstName(),
                        user.getLastName(), user.getPoints(), user.getProfilePicture());
            } else {
                System.out.println("Nutzer ist privat");
                return null;
              }
            }
            else{
               return null;
            }
    }*/
