package com.example.backend.service;

import com.example.backend.entity.ChessPuzzle;
import com.example.backend.entity.User;
import com.example.backend.exception.ApiRequestException;
import com.example.backend.repository.ChessPuzzleRepository;
import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChessPuzzleService {

    @Autowired
    private ChessPuzzleRepository chessPuzzleRepository;
    @Autowired
    private UserRepository userRepository;

    String line ="";

    public void uploadData(String puzzleFile){
         System.out.println("test");

                final String[] puzzles = puzzleFile.split("\n");
                final int numOfPuzzles = puzzles.length - 1;

                for (int i=1; i < puzzles.length; i++){
                    final String[] currentPuzzle = puzzles[i].split(",");
                    final String currentId = currentPuzzle[0];
                    ChessPuzzle cp2 = new ChessPuzzle();
                    cp2.setPuzzleId(currentPuzzle[0]);
                    cp2.setFen(currentPuzzle[1]);
                    cp2.setMoves(currentPuzzle[2]);
                    cp2.setThemes(currentPuzzle[7]);
                    chessPuzzleRepository.save(cp2);
                }
    }

    public List<String> getAllPuzzleIds(){
        List<ChessPuzzle> chessPuzzleList = chessPuzzleRepository.findAll();
        List<String> puzzleIds = new ArrayList<>();
        for(ChessPuzzle cp : chessPuzzleList){
            puzzleIds.add(cp.getPuzzleId());
        }
        return puzzleIds;
    }

    public String[] getMovesByPuzzleId(String puzzleId){
        ChessPuzzle cp = chessPuzzleRepository.findChessPuzzleByPuzzleId(puzzleId);
        return cp.getMoves().split(" ");
    }

    public List<String> getFenByPuzzleId(String puzzleId){
        ChessPuzzle cp = chessPuzzleRepository.findChessPuzzleByPuzzleId(puzzleId);
        List<String> fenlist = new ArrayList<>();
        fenlist.add(cp.getFen());
        return fenlist;
    }

    public List<String> getAllThemes(){
        List<ChessPuzzle> chessPuzzleList = chessPuzzleRepository.findAll();
        List<String> themes = new ArrayList<>();

        for(ChessPuzzle cp : chessPuzzleList){
            themes.add(cp.getThemes());
        }
        return themes;
    }
    public void increasePassed(String username, String puzzleId){
        User user = userRepository.findByUsername(username);
        List<String> list = user.getPassedList();
        if(!list.contains(puzzleId)) {
            user.setPassedPuzzle(user.getPassedPuzzle() + 1);
            user.checkAnerkennung();
            list.add(puzzleId);
            user.setPassedList(list);
            userRepository.save(user);
        }
    }
    public List<String> getAnerkennung(String username){
        User user = userRepository.findByUsername(username);
        String anerkennung = user.getAnerkennung();
        List<String> list = new ArrayList<>();
        list.add(anerkennung);
        return list;
    }
/*    public int getPassed(String username){
        User user = userRepository.findByUsername(username);
        return user.getPassedPuzzle();
    } */
}
