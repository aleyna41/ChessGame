package com.example.backend.controller;
import com.example.backend.DTO.ChessClubRequest;
import com.example.backend.entity.ChessClub;
import com.example.backend.exception.ApiRequestException;
import com.example.backend.repository.ChessClubRepository;
import com.example.backend.service.ChessClubService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin
public class ChessClubController {

    private final ChessClubService chessClubService;
    private final ChessClubRepository chessClubRepository;

    public ChessClubController(ChessClubService chessClubService, ChessClubRepository chessClubRepository) {
        this.chessClubService = chessClubService;
        this.chessClubRepository = chessClubRepository;
    }

    @GetMapping("/getChessClubNames")
    public List<String> getChessClubNames() {
        return chessClubService.getAllChessClubNames();
    }

    //das steht in user jetzt
    /*@GetMapping("/getChessClubsForUser/{member}")
    public List<String> getChessClubsForUser(@PathVariable String member){
        return chessClubService.getChessClubsForUser(member);
    }*/

    @GetMapping("/getMyChessClubNames/{username}")
    public List<String> getMyChessClubnames(@PathVariable String username) { return chessClubService.getMyChessClubNames(username); }

    @GetMapping("/getMembersForChessClub/{clubName}")
    public List<String> getMembersForChessClub(@PathVariable String clubName){
        return chessClubService.getMembersForChessClub(clubName);
    }

    @PostMapping("/createChessClub")
    public ResponseEntity<String> createChessClub(@RequestBody ChessClubRequest chessClubRequest) throws ApiRequestException{
        try{
            chessClubService.createChessClub(chessClubRequest.getMember(), chessClubRequest.getClubName());
            return ResponseEntity.ok().build();
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/joinChessClub")
    public ResponseEntity<String> joinChessClub(@RequestBody ChessClubRequest chessClubRequest) throws ApiRequestException{
        try{
            chessClubService.joinChessClub(chessClubRequest.getMember(), chessClubRequest.getClubName());
            return ResponseEntity.ok().build();
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }



}
