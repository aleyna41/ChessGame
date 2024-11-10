package com.example.backend.controller;

import com.example.backend.DTO.*;
import com.example.backend.entity.Friend;
import com.example.backend.entity.User;
import com.example.backend.exception.ApiRequestException;
import com.example.backend.repository.FriendRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.FriendService;
import com.example.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class FriendController {

    private final UserService userService; //warum wird das nicht erreicht?
    private final FriendService friendService;
    private final FriendRepository friendRepository;
    private final UserRepository userRepository;


    public FriendController(FriendService friendService, FriendRepository friendRepository, UserService userService, UserRepository userRepository) {
        this.friendService = friendService;
        this.friendRepository = friendRepository;
        this.userService = userService;
        this.userRepository = userRepository;

    }

    @PostMapping("/sendFriendRequest")
    public ResponseEntity<Void> sendFriendRequest(@RequestBody Map<String, String> request) throws ApiRequestException {
        try {
            String senderName = request.get("senderName");
            String receiverName = request.get("receiverName");

            friendService.sendFriendRequest(senderName, receiverName);
            return ResponseEntity.ok().build();

        } catch (ApiRequestException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/acceptFriendRequest")
    public ResponseEntity<Void> acceptFriendRequest(@RequestBody Map<String, String> request) throws ApiRequestException {
        try {
            String senderName = request.get("senderName");
            String receiverName = request.get("receiverName");

            //System.out.println(senderName);
            //System.out.println(receiverName);

            friendService.acceptFriendRequest(senderName, receiverName);
            return ResponseEntity.ok().build();
        } catch (Exception e){ //verbessern?
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @PostMapping("/rejectFriendRequest")
    public ResponseEntity<Void> rejectFriendRequest(@RequestBody Map<String, String> request) throws ApiRequestException{
        try {
            String senderName = request.get("senderName");
            String receiverName = request.get("receiverName");

            friendService.rejectFriendRequest(senderName, receiverName);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping ("/removeFriend/{senderName}/{receiverName}") //DELETE MAPPING GEHT NICHT
    public ResponseEntity<Void> removeFriend(@PathVariable String senderName, @PathVariable String receiverName) throws ApiRequestException {

        try {
            friendService.removeFriend(senderName, receiverName);
            return ResponseEntity.ok().build();
        } catch(Exception e){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @GetMapping("/getPendingFriendRequests/{receiverName}")
    public List<String> getPendingFriends(@PathVariable String receiverName) throws ApiRequestException {
        return friendService.getPendingFriendList(receiverName);
    }
    @GetMapping("/getFriends/{username}")
    public List<String> getFriends(@PathVariable String username) throws ApiRequestException {
        return friendService.getFriends(username);
    }

    @GetMapping("/getFriendList")
    public List<Friend> getFriendList(){
        return friendService.getFriendList();
    }

/* KOMMT NOCH



   /* @GetMapping("/getFriendProfile")
    public ResponseEntity<FriendProfileDTO> getFriendProfile(@RequestParam String username) {
        FriendProfileDTO friendProfile = friendService.getFriendProfile(username);
        if (friendProfile != null) {
            return ResponseEntity.ok(friendProfile);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }*/





}
