package com.example.backend.service;

import com.example.backend.entity.ChessClub;
import com.example.backend.entity.User;
import com.example.backend.exception.ApiRequestException;
import com.example.backend.repository.ChessClubRepository;
import com.example.backend.repository.UserRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Service
public class ChessClubService {
    private final UserService userService;
    private final UserRepository userRepository;
    private final ChessClubRepository chessClubRepository;
    private final ChatService chatService;


    public ChessClubService(UserService userService, UserRepository userRepository, ChessClubRepository chessClubRepository
    , ChatService chatService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.chessClubRepository = chessClubRepository;
        this.chatService = chatService;
    }

    public List<String> getAllChessClubNames() {
        List<ChessClub> chessClubs = chessClubRepository.findAll();
        List<String> chessClubNames = new ArrayList<>();

        for (ChessClub chessClub : chessClubs) {
            chessClubNames.add(chessClub.getClubName());
        }

        return chessClubNames;
    }


    public List<String> getMyChessClubNames(String username){
        List<ChessClub> chessClubs = chessClubRepository.findAll();
        List<String> myChessClubNames = new ArrayList<>();

        for (ChessClub chessClub : chessClubs) {
            if(chessClub.getMembers().contains(username)) {
                myChessClubNames.add(chessClub.getClubName());
            }
        }
        return myChessClubNames;
    }

    /*public List<String> getChessClubsForUser(String member) {
        List<ChessClub> clubs = chessClubRepository.findByMembers(member);
        List<String> clubNames = new ArrayList<>();
        for(ChessClub club: clubs){
            clubNames.add(club.getClubName());
        }
        return clubNames;
    }*/


    public void createChessClub(String creator, String clubName) throws ApiRequestException { //ergibt das sinn die methode void zu lassen?
        try {
            if (chessClubRepository.existsByClubName(clubName)) {
                throw new ApiRequestException("Der Schachclub existiert bereits.");
            }
            List<String> members = new ArrayList<>();
            ChessClub chessClub = new ChessClub(clubName, members);
            members.add(creator);
            chessClubRepository.save(chessClub);
            chatService.createClubChat(clubName);
        } catch(Exception e){
                throw new ApiRequestException("Der Schachclub existiert bereits.");
        }
    }


    public void joinChessClub(String member, String clubName) throws ApiRequestException { //sollte hier kein string sein?
        ChessClub chessClub = chessClubRepository.findByClubName(clubName);
        if(chessClub == null){
            throw new ApiRequestException("Schachclub nicht gefunden.");
        }
        if (chessClub.getMembers().contains(member)) {
            throw new ApiRequestException("Benutzer ist bereits Mitglied des Schachclubs.");
        }
        List<String> members = chessClub.getMembers();
        members.add(member);
        chessClub.setMembers(members);
        chessClubRepository.save(chessClub);
    }


    public List<String> getMembersForChessClub(String clubName) {
        List<String> members = chessClubRepository.findMembersByClubName(clubName);
        List<String> memberNames = new ArrayList<>();
        for (String member : members) {
            memberNames.add(member);
        }
        return memberNames;
    }









}
