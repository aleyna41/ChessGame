package com.example.backend.service;

import com.example.backend.DTO.PgnDTO;
import com.example.backend.entity.Game;
import com.example.backend.repository.GameRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PgnService {

    @Autowired
    GameRepository gameRepository;
    @Autowired
    GameService gameService;

    public List<String> uploadPgn(String pgnFile) {
        List<String> moves = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\[.*?\\]");
        Matcher matcher = pattern.matcher(pgnFile);

        int letzteKlammer = -1;

        while (matcher.find()) {
            letzteKlammer = matcher.end();
        }
        if (letzteKlammer != -1 && letzteKlammer < pgnFile.length()) {
            String allMoves = pgnFile.substring(letzteKlammer).trim();
            String[] splittedMoves = allMoves.split("\\s+");

            for (String moveToken : splittedMoves) {
                if (!moveToken.isEmpty() && !moveToken.endsWith(".")) {
                    moves.add(moveToken);
                }
            }
        }
        return moves;
    }

    public List<String> getGameSan(String gameName) {
        Game game = gameRepository.findByGameName(gameName);
        return game.getSanString();
    }

    public PgnDTO getGameData(String gameName) {
        Game game = gameRepository.findByGameName(gameName);
        PgnDTO pgnDTO = new PgnDTO();
        pgnDTO.setWhite(game.getPlayer1());
        pgnDTO.setBlack(game.getPlayer2());
        pgnDTO.setResult(game.getResult());

        String dateString;
        if (game.getCompletionDate()!=null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
             dateString = dateFormat.format(game.getCompletionDate());
        } else {
            dateString = "Datum ist leer";
        }
        pgnDTO.setDate(dateString);
        return pgnDTO;
    }

    public String createPgnString(String gameName) {
        List<String> san = getGameSan(gameName);
        PgnDTO pgnDto = getGameData(gameName);
        san.add(pgnDto.getResult());
        int i = 0;
        int z = 1;
        for (int a = 0; a < (int) Math.ceil((double) san.size() / 2) && i < san.size(); a++) {
            san.add(i, z + ".");
            i = i + 3;
            z++;
        }
        String tempok = san.toString().replaceAll(",", "");
        tempok = tempok.substring(1, tempok.length() - 1);
        pgnDto.setMoves(tempok);
        int round = z-1;
        String pgnFile = ("[Event '" + gameName + "']\n[Site '" + "SchachEP" + "']\n[Date '" + pgnDto.getDate() + "']\n[Round '" + round + "']\n[White '" + pgnDto.getWhite() + "']\n[Black '" + pgnDto.getBlack() + "']\n[Result '" + pgnDto.getResult() + "']\n" + pgnDto.getMoves());
        return pgnFile;
    }

    public File convertPgnToFile(String gameName) {
        File file = new File(gameName + ".pgn"); //Datei in die geschrieben werden soll

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file)); //Writer für Textdateien erstellt
            writer.write(createPgnString(gameName));
            writer.close();
            return file;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String saveManualFile(String gameName) throws IOException {
        File manualFile = convertPgnToFile(gameName);
        String fileName = manualFile.getName();

        Path uploadPath = Paths.get("PGN-Files");
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        String fileCode = RandomStringUtils.randomAlphanumeric(8) + ".pgn";
        try (InputStream inputStream = new FileInputStream(manualFile)) {
            Path filePath = uploadPath.resolve(fileCode + "-" + fileName + ".pgn");
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Datei konnte nicht gespeichert werden: " + fileName, ioe);
        }
        return fileCode;
    }

    private Path foundFile;

    public Resource getFileAsResource(String fileCode) throws IOException {
        Path dirPath = Paths.get("PGN-Files");
        Files.list(dirPath).forEach(file -> {
            if (file.getFileName().toString().startsWith(fileCode)) {
                foundFile = file;
            }
        });
        if (foundFile != null) {
            return new UrlResource(foundFile.toUri());
        }
        return null;
    }
}