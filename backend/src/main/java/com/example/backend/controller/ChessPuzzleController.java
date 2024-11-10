package com.example.backend.controller;

import com.example.backend.DTO.IncreasePassedRequest;
import com.example.backend.entity.User;
import com.example.backend.exception.ApiRequestException;
import com.example.backend.service.ChessPuzzleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequestMapping("/puzzle")
@RestController
@CrossOrigin

public class ChessPuzzleController {

    private final ChessPuzzleService cps;
    public ChessPuzzleController(ChessPuzzleService cps){
        this.cps = cps;

    }

    @PostMapping("/uploadData")
    public ResponseEntity<Void> uploadData(@RequestBody MultipartFile puzzleFile) throws IOException {
        byte[] arr = puzzleFile.getBytes();
        String csvFile = new String(arr);
        cps.uploadData(csvFile);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getAllPuzzleIds")
    public List<String> getFen(){
        return cps.getAllPuzzleIds();
    }

    @GetMapping("/getAllThemes")
    public List<String> getAllThemes() { return cps.getAllThemes(); }

    @GetMapping("/getFenByPuzzleId/{puzzleId}")
    public List<String> getFenByPuzzleId(@PathVariable String puzzleId){
        return cps.getFenByPuzzleId(puzzleId);
    }

    @PostMapping("/increasePassed")
    public void increasePassed(@RequestBody IncreasePassedRequest increasePassedRequest) {
        cps.increasePassed(increasePassedRequest.getUsername(), increasePassedRequest.getPuzzleId());
    }

//    @GetMapping("/getPassed/{username}")
//    public int getPassed(@PathVariable String username) {return cps.getPassed(username);}

    @GetMapping("/getMovesByPuzzleId/{puzzleId}")
    public String[] getMovesByTheme(@PathVariable String puzzleId) {
        return cps.getMovesByPuzzleId(puzzleId);
    }
    @GetMapping("/getAnerkennung/{username}")
    public List<String> getAnerkennung(@PathVariable String username){
        return cps.getAnerkennung(username);
    }
}
