package com.example.backend.service;

import com.example.backend.entity.*;
import com.example.backend.entity.GameRequestStatus;
//import com.example.backend.entity.Timer;
import com.example.backend.exception.ApiRequestException;
import com.example.backend.repository.GameRepository;
import com.example.backend.repository.UserRepository;
import com.github.bhlangonijr.chesslib.Board;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GameService {

    private final GameRepository gameRepository;
    private final UserRepository userRepository;

    private Map<String, ChessTimer> gameTimers = new HashMap<>(); //das kommt nicht in den konstruktor

    public GameService(GameRepository gameRepository, UserRepository userRepository) {
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
    }

//-----------------------------------Invites--------------------------------------

    public void sendGameRequest(String gameName, String senderUsername, String receiverUsername, int time) throws ApiRequestException {
        //gibts die namen?
        if (senderUsername == null || receiverUsername == null) {
            throw new ApiRequestException("Sender or receiver not found");
        }
        //sind die namen registrierte user?
        User sender = userRepository.findByUsername(senderUsername);
        User receiver = userRepository.findByUsername(receiverUsername);
        if (sender == null || receiver == null) {
            throw new ApiRequestException("Sender or receiver not found");
        }
        //spiel muss eindeutig sein
        else if (gameRepository.existsByGameName(gameName)) {
            System.out.println("spiel existiert");
            throw new ApiRequestException("spiel existiert");

        } else {
            List<String> anfang = new ArrayList<>();
            anfang.add("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
            ChessTimer timer = new ChessTimer(time*60);
            Game gameRequest = new Game(gameName, senderUsername, receiverUsername, anfang, null, false, time);
            gameRequest.setStatus(GameRequestStatus.PENDING);
            gameRequest.setStreaming(false);
            gameTimers.put(gameName, timer);
            gameRepository.save(gameRequest);
        }
    }


    public void acceptGameRequest(String gameName) throws ApiRequestException {
        try {
            Game gameRequest = gameRepository.findByGameName(gameName);
            if (gameRequest.getStatus() == GameRequestStatus.PENDING) {
                gameRequest.setStatus(GameRequestStatus.ACCEPTED);
                gameRequest.setGameStarted(true);
                gameRepository.save(gameRequest);
                gameTimers.get(gameName).startGame();
            }
        } catch (Exception e) {
            throw new ApiRequestException("Anfrage kann nicht angenommen werden");
        }
    }

    public void rejectGameRequest(String gameName) throws ApiRequestException {
        try {
            Game gameRequest = gameRepository.findByGameName(gameName);
            if (gameRequest.getStatus() == GameRequestStatus.PENDING) {
                gameRequest.setStatus(GameRequestStatus.REJECTED);
                gameRepository.delete(gameRequest);
            }
        } catch (Exception e) {
            //EXCEPTION WERFEN
            throw new ApiRequestException("Anfrage kann nicht angenommen werden");
        }
    }

//-----------------GAME-------------------------------------

    public void updateBoard(String gameName, String fenString, String sanString) {
        //Move move = new Move(Square.E2, Square.E4);// ---> StringAusgabe hier lautet e2e4
        Game chessBoardUpdate = gameRepository.findByGameName(gameName);

        List<String> sanStrings = gameRepository.findByGameName(gameName).getSanString();
        List<String> fenStrings = gameRepository.findByGameName(gameName).getFenString();

        sanStrings.add(sanString);
        fenStrings.add(fenString);

        chessBoardUpdate.setSanString(sanStrings);
        chessBoardUpdate.setFenString(fenStrings);

        gameRepository.save(chessBoardUpdate);
    }

    public void switchTimerWhenMove(String gameName){
        gameTimers.get(gameName).switchTurn();
    }

    public void startTimer(String gameName){
        gameTimers.get(gameName).startGame();
    }

    public void stopTimer(String gameName){
        gameTimers.get(gameName).stopTimer();
    }

    public List<Integer> getFormattedTime(String gameName){
        List<Integer> timers = new ArrayList<>();
        timers.add(gameTimers.get(gameName).getWhiteTimer());
        timers.add(gameTimers.get(gameName).getBlackTimer());
        return timers;
    }
    public String getResult(String gameName){
        return  "\"" + gameRepository.findByGameName(gameName).getResult() + "\"";
    }
    public List<String> getAllGames(){
        return gameRepository.findAllGameNames();
    }
    public List<String> getStreams(){
        return gameRepository.findStreams();}
    public void startLivestream(String gameName){
        Game game = gameRepository.findByGameName(gameName);
        game.setStreaming(true);
        gameRepository.save(game);
    }
    public List<String> getGameInfo(String gameName) { //ohne timer
        return gameRepository.findByGameName(gameName).getFenString();
    }

    public void addPoints(String userName){
        User winner = userRepository.findByUsername(userName);
        winner.setPoints(winner.getPoints()+10);
        userRepository.save(winner);
    }

    public void removePoints(String userName){
        User loser = userRepository.findByUsername(userName);
        loser.setPoints(loser.getPoints()-10);
        userRepository.save(loser);
    }


    public List<String> getPendingGameRequests(String player2) throws ApiRequestException {
        List<Game> games = gameRepository.findByPlayer2AndStatus(player2, GameRequestStatus.PENDING);
        List<String> gameNames = new ArrayList<>();
        for (Game game : games) {
            if (player2.equals(game.getPlayer2())){
                gameNames.add(game.getGameName());
            }
        }
        return gameNames;
    }

    public List<String> getAcceptedGameRequests(String player) throws ApiRequestException {
        List<Game> gamesForPlayer2 = gameRepository.findByPlayer1AndStatusAndResult(player, GameRequestStatus.ACCEPTED, null);
        List<Game> gamesForPlayer1 = gameRepository.findByPlayer2AndStatusAndResult(player, GameRequestStatus.ACCEPTED, null);

        List<String> gameNames = new ArrayList<>();

        for (Game game : gamesForPlayer1) {
            if(!game.getPlayer1().equals("Bot Yasir") && !game.getPlayer2().equals("Bot Yasir")) {
                if (player.equals(game.getPlayer2())) {
                    gameNames.add(game.getGameName());
                }
            }
        }
        for (Game game : gamesForPlayer2) {
            if(!game.getPlayer1().equals("Bot Yasir") && !game.getPlayer2().equals("Bot Yasir")) {
                if (player.equals(game.getPlayer1())) {
                    gameNames.add(game.getGameName());
                }
            }
        }
        return gameNames;
    }

    public List<String> getPlayersByGamename(String gameId){
        List<String> players = new ArrayList<>();
        players.add(gameRepository.findByGameName(gameId).getPlayer1());
        players.add(gameRepository.findByGameName(gameId).getPlayer2());
        return players;
    }
    public boolean existGamesofPlayers(String player1, String player2){
        return gameRepository.existsByPlayer1AndPlayer2AndResult(player1, player2, null);
    }
    //------------------------Für Game-History---------------------------------------------

    public List<String> getGameSans(Long gameId){
        List<String> san;
        Optional<Game> game = gameRepository.findById(gameId);
        if (game.isPresent()){
            san = game.get().getSanString();
            return san;
        } else return null;
    }

    public String getWhite(Long gameId){
        Optional<Game> game = gameRepository.findById(gameId);
        if (game.isPresent()) {
            String player1 = game.get().getPlayer1();
            return "\"" + player1 + "\"";
        } else return null;
    }

    //------------------------Für Game-Result---------------------------------------------

    public Map<String, Integer> getWinnerAndElo(Long gameId){
        Optional<Game> game = gameRepository.findById(gameId);
        String winner;
        int elo;
        Map<String, Integer> winnerMap = new HashMap<>();
        if (game.isPresent() && game.get().getResult().equals("1-0")) {
            winner = game.get().getPlayer1();
            elo = userRepository.findByUsername(winner).getPoints();
            winnerMap.put(winner, elo);
        } else if (game.isPresent() && game.get().getResult().equals("0-1")) {
            winner = game.get().getPlayer2();
            elo = userRepository.findByUsername(winner).getPoints();
            winnerMap.put(winner, elo);
        }
        return winnerMap;
    }

    public Map<String, Integer> getLooserAndElo(Long gameId){
        Optional<Game> game = gameRepository.findById(gameId);
        String looser;
        int elo;
        Map<String, Integer> looserMap = new HashMap<>();
        if (game.isPresent() && game.get().getResult().equals("1-0")) {
            looser = game.get().getPlayer2();
            elo = userRepository.findByUsername(looser).getPoints();
            looserMap.put(looser, elo);
        } else if (game.isPresent() && game.get().getResult().equals("0-1")) {
            looser = game.get().getPlayer1();
            elo = userRepository.findByUsername(looser).getPoints();
            looserMap.put(looser, elo);
        }
        return looserMap;
    }

    public Map<String, Integer> getWhiteAndElo(Long gameId){
        Optional<Game> game = gameRepository.findById(gameId);
        if (game.isPresent()) {
            String white = game.get().getPlayer1();
            Integer elo = userRepository.findByUsername(white).getPoints();
            Map<String, Integer> whiteMap = new HashMap<>();
            whiteMap.put(white, elo);
            return whiteMap;
        } else return null;
    }

    public Map<String, Integer> getBlackAndElo(Long gameId){
        Optional<Game> game = gameRepository.findById(gameId);
        if (game.isPresent()) {
            String black = game.get().getPlayer2();
            Integer elo = userRepository.findByUsername(black).getPoints();
            Map<String, Integer> blackMap = new HashMap<>();
            blackMap.put(black, elo);
            return blackMap;
        } else return null;
    }

    public Long getGameId(String gameName){
        Game game = gameRepository.findByGameName(gameName);
        return game.getId();
    }


    public void sendDrawRequest(String gameName, String senderUsername, String receiverUsername) throws ApiRequestException {
        if(!gameRepository.existsByGameName(gameName) || userRepository.findByUsername(senderUsername) == null || userRepository.findByUsername(receiverUsername) == null){
            throw new ApiRequestException("Sender or receiver not found");
        }
        else {
            Game drawRequest = gameRepository.findByGameName(gameName);
            drawRequest.setDrawRequestStatus(DrawRequestStatus.PENDING);
            gameRepository.save(drawRequest);
        }
    }

    public void acceptDrawRequest(String gameName) throws ApiRequestException {
        try {
            Game drawRequest = gameRepository.findByGameName(gameName);
            if(drawRequest.getDrawRequestStatus() == DrawRequestStatus.PENDING){
                drawRequest.setDrawRequestStatus(DrawRequestStatus.ACCEPTED);

                drawRequest.setResult("1/2-1/2");
                drawRequest.setCompletionDate(new Date());
                gameTimers.get(gameName).stopTimer();

                gameRepository.save(drawRequest);

            }
        } catch (Exception e) {
            throw new ApiRequestException("Anfrage kann nicht angenommen werden");
        }
    }

    public void rejectDrawRequest(String gameName) throws ApiRequestException {
        try {
            Game drawRequest = gameRepository.findByGameName(gameName);
            if(drawRequest.getDrawRequestStatus() == DrawRequestStatus.PENDING){
                drawRequest.setDrawRequestStatus(DrawRequestStatus.REJECTED);
                //spiel geht weiter, es passiert nichts
                gameRepository.save(drawRequest);
            }
        } catch (Exception e) {
            throw new ApiRequestException("Anfrage kann nicht angenommen werden");
        }
    }
    public boolean isDrawRequested(String gameName){
        if(gameRepository.findByGameName(gameName).getDrawRequestStatus()==null){
            return false;
        } else if (gameRepository.findByGameName(gameName).getDrawRequestStatus()== DrawRequestStatus.REJECTED) {
            return false;
        }
        return true;
    }


    public void surrender(String gameName, String senderUsername, String receiverUsername){
        Game game = gameRepository.findByGameName(gameName);
        if(Objects.equals(senderUsername, game.getPlayer1())){
            game.setResult("0-1");
        }
        if(Objects.equals(senderUsername, game.getPlayer2())){
            game.setResult("1-0");
        }
        game.setCompletionDate(new Date());
        gameRepository.save(game);
        gameTimers.get(gameName).stopTimer();
    }

//------------------------DATENBANKTESTS-----------------------------------------------------

    public List<Game> getGames() {
        List<Game> acceptedGames = gameRepository.findByStatus(GameRequestStatus.ACCEPTED);
        if (acceptedGames.isEmpty()) {
            System.out.println("Keine akzeptierten Spiele gefunden.");
        } return acceptedGames;
    }

    public List<Game> getPendingGames() {
        List<Game> acceptedGames = gameRepository.findByStatus(GameRequestStatus.PENDING);
        if (acceptedGames.isEmpty()) {
            System.out.println("Keine akzeptierten Spiele gefunden.");
        } return acceptedGames;
    }
}