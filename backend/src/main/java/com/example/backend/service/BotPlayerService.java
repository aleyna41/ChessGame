package com.example.backend.service;

import com.example.backend.entity.ChessTimer;
import com.example.backend.entity.User;
import com.example.backend.repository.GameRepository;
import com.example.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter; //neuer import für ausgabe
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Service
public class BotPlayerService {

    private Process stockfishProcess;
    private BufferedReader stockfishInput;
    private PrintWriter stockfishOutput;
    private final UserRepository userRepository;

    //testen ohne docker:
    //private static final String stockfishPath = "backend/src/main/resources/stockfish_14_x64_avx2.exe";

    //testen mit docker(ubuntu):
    private static final String stockfishPath = "/usr/app/stockfish";




    public BotPlayerService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }




    public void initializeBot() throws IOException {
        try{
            stockfishProcess = new ProcessBuilder(stockfishPath).start();

            // Kommunikation in streams
            stockfishInput = new BufferedReader(new InputStreamReader(stockfishProcess.getInputStream()));
            stockfishOutput = new PrintWriter(stockfishProcess.getOutputStream(), true);

            // stockFish ready?
            sendCommand("uci");
            waitForReady();
            System.out.println("Bot erfolgreich initialisiert.");
        } catch (IOException e) {
            System.out.println("Fehler bei der Initialisierung: " + e.getMessage());
        }

    }




    // fen wird aus repository über gameName gesucht
    public String calculateBestMove(String fen, int depth) {
        sendCommand("position fen " + fen);
        sendCommand("go depth " + depth);
        return readBestMove();
    }

    public String calculateBestMoveForAssistant(String fen){
        sendCommand("position fen " + fen);
        sendCommand("go depth " + 5);
        return readBestMove();
    }

    private void waitForReady() {
        try {
            String response;
            while ((response = stockfishInput.readLine()) != null) {
                if (response.equals("uciok")) {
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Bot liefert keine Antwort " + e.getMessage());
        }
    }






    public void deactivateBot(){
        System.out.println("Bot erfolgreich deaktiviert.");
        stockfishOutput.println("quit");
    }

    private void sendCommand(String command) {
        stockfishOutput.println(command);
    }

    private String readBestMove() {
       try{
           // sucht besten Zug
           String response;
           do {
               response = readStockfishOutput();
           } while (response != null && !response.startsWith("bestmove"));
           // Wenn response null ist oder nicht mit "bestmove" beginnt, gib null zurück
           if (response == null || !response.startsWith("bestmove")) {
               System.err.println("Kein bester Zug gefunden: " + response);
               return null;
           }
           // besten Zug entnehmen
           String[] parts = response.split(" ");
           String move;
           if (parts.length > 1) {
               move = parts[1];
           } else {
               move = null;
           }
           return "\"" + move + "\""; //für frontend
       } catch(NullPointerException npe) {
           System.err.println("beste Move iwie null: " + npe.getMessage());
           throw npe;
       }
    }


    private String readStockfishOutput() {
        // Lese die Ausgabe von Stockfish
        try {
            return stockfishInput.readLine();
        } catch (IOException e) {
            System.err.println("Fehler beim lesen: " + e.getMessage());
            return null;
        }
    }


    public void payWithPoint(String playerName){
        User player = userRepository.findByUsername(playerName);
        player.setPoints(player.getPoints()-1);
        userRepository.save(player);
    }



}



