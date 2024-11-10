package com.example.backend.controller;


import com.example.backend.DTO.FileUploadResponse;
import com.example.backend.DTO.PgnDTO;
import com.example.backend.repository.GameRepository;
import com.example.backend.service.PgnService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;


import java.io.*;
import java.util.List;

@RestController
@CrossOrigin
public class PgnController {

    private final PgnService pgnService;
    private final GameRepository gameRepository;

    public PgnController(PgnService pgnService, GameRepository gameRepository) {
        this.pgnService = pgnService;
        this.gameRepository = gameRepository;
    }

    @PostMapping("/uploadPgn")
    public List<String> uploadPgn(@RequestBody MultipartFile pgnFile) throws IOException {
        byte[] arr = pgnFile.getBytes();
        String pgnFileS = new String(arr);
        return pgnService.uploadPgn(pgnFileS);
    }

    @GetMapping("/getGameSan")
    public List<String> getGameSan(@RequestParam String gameName){
        return pgnService.getGameSan(gameName);
    }

    @GetMapping("/getGameData")
    public PgnDTO getGameData(@RequestParam String gameName){
        return pgnService.getGameData(gameName);
    }

    @GetMapping(value = "/createPgnString/{gameName}", produces = MediaType.TEXT_PLAIN_VALUE)
    public String createPgnString(@PathVariable String gameName) {
        System.out.println(gameName);
        return pgnService.createPgnString(gameName); }

    @GetMapping("/convertPgnToFile/{gameName}")
    public File convertPgnToFile(@PathVariable String gameName) {
        return pgnService.convertPgnToFile(gameName);
    }

    @GetMapping("/downloadFile/{fileCode}")
    public ResponseEntity<?> downloadFile(@PathVariable("fileCode") String fileCode) {

        Resource resource = null;
        try {
            resource = pgnService.getFileAsResource(fileCode);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
        if(resource == null) {
            return new ResponseEntity<>("Datei wurde nicht gefunden", HttpStatus.NOT_FOUND);
        }

        String contentType = "application/octet-stream";
        String headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).header(HttpHeaders.CONTENT_DISPOSITION, headerValue).body(resource);
    }

    @GetMapping("/saveManualFile/{gameId}")
    public ResponseEntity<String> saveManuelFile(@PathVariable Long gameId) throws IOException {
        String gameName = gameRepository.findById(gameId).get().getGameName();
        String filecode = pgnService.saveManualFile(gameName);

        FileUploadResponse response = new FileUploadResponse();
        response.setFileName(gameName);
        response.setDownloadUri("/downloadFile/" + filecode);
        String downloadUri = "\"" + response.getDownloadUri() + "\"";
        return new ResponseEntity<>(downloadUri, HttpStatus.OK);
    }

}
