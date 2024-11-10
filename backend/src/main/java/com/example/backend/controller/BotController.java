package com.example.backend.controller;

import com.example.backend.service.BotPlayerService;
import com.example.backend.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@CrossOrigin
public class BotController {


    private final GameService gameService;
    private final BotPlayerService botPlayer;



    @Autowired  //damit botPlayer autwired wird, sonst  bean nicht gefunden
    public BotController(BotPlayerService botPlayer, GameService gameService) {
        this.botPlayer = botPlayer;
        this.gameService = gameService;
    }


    @PostMapping("/initializeBot")
    public ResponseEntity<Void> initializeBot() throws IOException {
        botPlayer.initializeBot();
        return ResponseEntity.ok().build();
    }




    // depth festgelegt: hier 5
    @GetMapping("/calculateBestMoveForAssistant/{gameName}")
    public String calculateBestMoveForAssistant(@PathVariable String gameName){
        return botPlayer.calculateBestMoveForAssistant(gameService.getGameInfo(gameName).get(gameService.getGameInfo(gameName).size()-1));

    }

    @GetMapping("/calculateBestMove/{gameName}/{depth}")
    public String calculateBestMove(@PathVariable String gameName, @PathVariable int depth) {
        return botPlayer.calculateBestMove(gameService.getGameInfo(gameName).get(gameService.getGameInfo(gameName).size()-1), depth);
    }

    @PostMapping("/payWithPoint")
    public ResponseEntity<Void> payWithPoint(@RequestBody String playerName){
        botPlayer.payWithPoint(playerName);
        return ResponseEntity.ok().build();
    }



    @PostMapping("/deactivateBot")
    public ResponseEntity<Void> deactivateBot(){
        botPlayer.deactivateBot();
        return ResponseEntity.ok().build();
    }











}
