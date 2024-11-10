package com.example.backend.controller;

import com.example.backend.DTO.ChessGameRequest;
import com.example.backend.DTO.DrawRequest;
import com.example.backend.DTO.GameRequest;
import com.example.backend.entity.Game;
import com.example.backend.exception.ApiRequestException;
import com.example.backend.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/game")
@CrossOrigin
public class GameController {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/sendGameRequest")
    public ResponseEntity<Void> sendGameRequest(@RequestBody GameRequest request) throws ApiRequestException {

            gameService.sendGameRequest(request.getGameName(), request.getPlayer1(), request.getPlayer2(), request.getTime());
            return ResponseEntity.ok().build();

    }


    @PostMapping("/rejectGameRequest")
    public ResponseEntity<Void> rejectGameRequest(@RequestBody String gameName) throws ApiRequestException {
        try {
            gameService.rejectGameRequest(gameName);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/acceptGameRequest")
    public ResponseEntity<Void> acceptGameRequest(@RequestBody String gameName) throws ApiRequestException {
        try {

            gameService.acceptGameRequest(gameName);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /*
    @GetMapping("/getPlayers/{gameName}")
    public List<String> getPlayers(@PathVariable String gameName) throws ApiRequestException{
        return gameService.getPlayers(gameName);
    }

     */


    @PostMapping("/updateGame")
    public ResponseEntity<Void> updateGame(@RequestBody ChessGameRequest chessGameRequest) {
        try {
            gameService.updateBoard(chessGameRequest.getGameName(), chessGameRequest.getFenString(), chessGameRequest.getSanString());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).build();
        }
    }

    @PostMapping("/startTimer")
    public ResponseEntity<Void> startTimer(@RequestBody String gameName) {
        try {
            gameService.startTimer(gameName);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/switchTimer")
    public ResponseEntity<Void> switchTimer(@RequestBody String gameName) {
        try {
            gameService.switchTimerWhenMove(gameName);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }

    @PostMapping("/stopTimer")
    public ResponseEntity<Void> stopTimer(@RequestBody String gameName) {
        try {
            gameService.stopTimer(gameName);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/getFormattedTime/{gameName}")
    public List<Integer> getFormattedTime(@PathVariable String gameName) {
        try {

            return gameService.getFormattedTime(gameName);
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping("/getResult/{gameName}")
    public String getResult(@PathVariable String gameName){
        return gameService.getResult(gameName);
    }

    @PostMapping("/addPoints")
    public ResponseEntity<Void> addPoints(@RequestBody String userName) {
        try {
            gameService.addPoints(userName);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/removePoints")
    public ResponseEntity<Void> removePoints(@RequestBody String userName) {
        try {
            gameService.removePoints(userName);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/getAllGames")
    public List<String> getAllGames() {
        return gameService.getAllGames();
    }

    @GetMapping("/getStreams")
    public List<String> getStreams() {
        return gameService.getStreams();
    }

    @PostMapping("/startLivestream")
    public ResponseEntity startLivestream(@RequestBody String gameName) {
        try {
            gameService.startLivestream(gameName);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/getGameInfo/{gameName}")
    public List<String> getGameInfo(@PathVariable String gameName) {
        return gameService.getGameInfo(gameName);
    }
    @GetMapping("/getGamesof/{player1}/{player2}")
    public boolean existGamesofPlayers(@PathVariable String player1, @PathVariable String player2){
        return gameService.existGamesofPlayers(player1, player2);
    }

    // ----------------------Für Game-History---------------------------------------------------

    @GetMapping("/getGameSans/{gameId}")
    public ResponseEntity<List<String>> getGameSans(@PathVariable Long gameId) {
        if (gameService.getGameSans(gameId) != null) {
            return ResponseEntity.ok(gameService.getGameSans(gameId));
        } else return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/getPlayers/{gameId}")
    public List<String> getPlayersByGameId(@PathVariable String gameId) {
        return gameService.getPlayersByGamename(gameId);
    }

    @GetMapping("/getWhite/{gameId}")
    public ResponseEntity<String> getWhite(@PathVariable Long gameId) {
        if (gameService.getWhite(gameId) != null) {
            return ResponseEntity.ok(gameService.getWhite(gameId));
        } else return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

// ----------------------Für Game-Result---------------------------------------------------

    @GetMapping("/getWinnerAndElo/{gameId}")
    public ResponseEntity<Map<String, Integer>> getWinnerAndElo(@PathVariable Long gameId) {
        if (gameService.getWinnerAndElo(gameId) != null) {
            return ResponseEntity.ok((gameService.getWinnerAndElo(gameId)));
        } else return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/getLooserAndElo/{gameId}")
    public ResponseEntity<Map<String, Integer>> getLooserAndElo(@PathVariable Long gameId) {
        if (gameService.getLooserAndElo(gameId) != null) {
            return ResponseEntity.ok((gameService.getLooserAndElo(gameId)));
        } else return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/getWhiteAndElo/{gameId}")
    public ResponseEntity<Map<String, Integer>> getWhiteAndElo(@PathVariable Long gameId) {
        if (gameService.getWhiteAndElo(gameId) != null) {
            return ResponseEntity.ok((gameService.getWhiteAndElo(gameId)));
        } else return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/getBlackAndElo/{gameId}")
    public ResponseEntity<Map<String, Integer>> getBlackAndElo(@PathVariable Long gameId) {
        if (gameService.getBlackAndElo(gameId) != null) {
            return ResponseEntity.ok((gameService.getBlackAndElo(gameId)));
        } else return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/getGameId/{gameName}")
    public ResponseEntity<Long> getGameId(@PathVariable String gameName) {
        return ResponseEntity.ok(gameService.getGameId(gameName));
    }

//----------------------DATENBANKTESTS---------------------------------------------------------

    @GetMapping("/getGames")
    public List<Game> getGames() {
        return gameService.getGames();
    }

    @GetMapping("/getPendingGames")
    public List<Game> getPendingGames() {
        return gameService.getPendingGames();
    }

    @GetMapping("/getPendingGameRequests/{player2}")
    public List<String> getPendingGameRequests(@PathVariable String player2) throws ApiRequestException {
        return gameService.getPendingGameRequests(player2);
    }

    @GetMapping("/getMyGames/{player}")
    public List<String> getMyGames(@PathVariable String player) throws ApiRequestException {
        return gameService.getAcceptedGameRequests(player);
    }
    /*
    @PostMapping("/uploadPgn")
    public ResponseEntity<String> uploadPgn(@RequestParam String pgnFile){
        gameService.uploadPgn(pgnFile);
        return ResponseEntity.ok(pgnFile);
    }
    */

//----------------------drawRequest------------------------------------------------------

    @PostMapping("/sendDrawRequest")
    public ResponseEntity<Void> sendDrawRequest(@RequestBody DrawRequest drawRequest) throws ApiRequestException {
        try {
            gameService.sendDrawRequest(drawRequest.getGameName(), drawRequest.getReceiverUserName(), drawRequest.getSenderUserName());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        }
    }

    @PostMapping("/acceptDrawRequest")
    public ResponseEntity<Void> acceptDrawRequest(@RequestBody String gameName) throws ApiRequestException {
        try {
            gameService.acceptDrawRequest(gameName);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        }
    }

    @PostMapping("/rejectDrawRequest")
    public ResponseEntity<Void> rejectDrawRequest(@RequestBody String gameName) throws ApiRequestException{
        try{
            gameService.rejectDrawRequest(gameName);
            return ResponseEntity.ok().build();
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        }
    }
    @GetMapping("/isDrawRequested/{gameName}")
    public boolean isDrawRequested(@PathVariable String gameName){
        return gameService.isDrawRequested(gameName);
    }

    @PostMapping("/surrender")
    public ResponseEntity<Void> surrender(@RequestBody DrawRequest surrender){
        try{
            gameService.surrender(surrender.getGameName(), surrender.getSenderUserName(), surrender.getReceiverUserName());
            return ResponseEntity.ok().build();
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        }
    }


}