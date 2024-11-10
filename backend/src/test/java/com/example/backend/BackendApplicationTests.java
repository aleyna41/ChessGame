package com.example.backend;

import com.example.backend.DTO.*;
import com.example.backend.entity.*;
import com.example.backend.exception.ApiRequestException;
import com.example.backend.repository.GameRepository;
import com.example.backend.repository.SessionTokenRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.*;
import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class BackendApplicationTests {

    @Test
    void contextLoads() {
    }

    private UserService userService;
    private FriendService friendService;
    private SecurityService securityService;
    private GameService gameService;
    private UserRepository userRepository;
    private ChessClubService chessClubService;
    private GameRepository gameRepository;


    @Autowired
    public BackendApplicationTests(GameRepository gameRepository, UserService userService, FriendService friendService, SecurityService securityService, GameService gameService, ChessClubService chessClubService, UserRepository userRepository) {
        this.userService = userService;
        this.friendService = friendService;
        this.securityService = securityService;
        this.gameService = gameService;
        this.userRepository = userRepository;
        this.chessClubService = chessClubService;
        this.gameRepository = gameRepository;
    }

    //Zyklus 2 Modultests:
    /*@Test
    void testCreateChessClub() throws ApiRequestException {
        // Vorherige Anzahl der Chessclubs speichern
        int initialClubCount = chessClubService.getAllChessClubNames().size();

        // Club erstellen
        chessClubService.createChessClub("myrlxxn", "OGclub");

        // Überprüfen, ob die Anzahl der Clubs zugenommen hat
        assertEquals(initialClubCount + 1, chessClubService.getAllChessClubNames().size());

        // Überprüfen, ob der erstellte Club vorhanden ist
        assertTrue(chessClubService.getAllChessClubNames().contains("OGclub"));
    }*/

    /*@Test
        void testGetLeaderboard() throws ApiRequestException {
            // Profile erstellen
            SignUpRequest signUpRequest = new SignUpRequest
                    ("myrlxxn", "1234","Marleen", "Gollan", "aleyna200342@gmail.com","2002-07-04");
            userService.registerUser(signUpRequest);
            SignUpRequest signUpRequest2 = new SignUpRequest
                    ("myrlxxn2", "1234", "Marleen2", "Gollan2", "marleen.gollan@stud.uni-due.de", "2002-07-04");
            userService.registerUser(signUpRequest2);
            userRepository.findByUsername(signUpRequest.getUsername()).setPoints(500);
            userRepository.findByUsername(signUpRequest2.getUsername()).setPoints(600);

            System.out.println(userRepository.findAll());
            // Leaderboard aufrufen
            List<LeaderboardDTO> leaderboard = userService.getLeaderboard();

            // Prüfen, dass nicht null
            assertNotNull(leaderboard);


            // Überprüfen, ob die Reihenfolge im Leaderboard korrekt ist (nach Punkten absteigend)
            assertTrue(leaderboard.get(0).getPoints() >= leaderboard.get(1).getPoints());
        }*/

    //Zyklus 3 Modultest Livestram und Gamehistoy;

    /*@Test
    void livestreams() throws ApiRequestException {
        SignUpRequest signUpRequest = new SignUpRequest
                ("myrlxxn", "1234","Marleen", "Gollan", "aleyna200342@gmail.com","2002-07-04");
        userService.registerUser(signUpRequest);
        SignUpRequest signUpRequest2 = new SignUpRequest
                ("myrlxxn2", "1234", "Marleen2", "Gollan2", "marleen.gollan@stud.uni-due.de", "2002-07-04");
        userService.registerUser(signUpRequest2);

        //anfrage senden
        gameService.sendGameRequest("Spiel1","myrlxxn", "myrlxxn2" , 3);
        System.out.println("erhaltene Anfrage von: " +gameService.getPendingGameRequests("myrlxxn2"));
        //anfrage annehmen
        gameService.acceptGameRequest("Spiel1");

        gameService.startLivestream("Spiel1");

        Game game = gameRepository.findByGameName("Spiel1");
        // Überprüfen, ob der Livestream gestartet wurde
        assertTrue(game.isStreaming());

        // Überprüfen, ob der Livestream in der Liste der Livestreams ist
        assertTrue(gameService.getStreams().contains("Spiel1"));

    }*/

   /* @Test
    void gameHistoryTest() throws ApiRequestException {
        // User registrieren
        SignUpRequest signUpRequest = new SignUpRequest
                ("myrlxxn", "1234","Marleen", "Gollan", "aleyna200342@gmail.com","2002-07-04");
        userService.registerUser(signUpRequest);
        SignUpRequest signUpRequest2 = new SignUpRequest
                ("myrlxxn2", "1234", "Marleen2", "Gollan2", "marleen.gollan@stud.uni-due.de", "2002-07-04");
        userService.registerUser(signUpRequest2);
        // Game hinzufügen
        List<String> temp = new ArrayList<>(Arrays.asList("e4", "e5", "d4", "exd4", "Qxd4", "Nc6", "Qe3", "Nf6", "Nc3", "Bb4", "Bd2", "O-O", "O-O-O", "Re8", "Qq3", "Rxe4", "a3", "1-0"));
        Game gametest= new Game("test", "myrlxxn", "myrlxxn2", null, temp, true, 0);
        gametest.setResult("1-0");
        gametest.setCompletionDate(new Date());
        gameRepository.save(gametest);
        // Ist ein game in Gamehistory?
        assertEquals(1, userService.getGameHistory("myrlxxn").size());
        // Ein fertig gespieltes und ein nicht fertig gespieltes game hinzufügen
        Game gametest2= new Game("test", "myrlxxn2", "myrlxxn", null, temp, true, 0);
        gametest2.setResult("1/2-1/2");
        gametest2.setCompletionDate(new Date());
        gameRepository.save(gametest2);
        Game gametest3= new Game("test", "myrlxxn", "myrlxxn2", null, temp, true, 0);
        gameRepository.save(gametest3);
        // Sind zwei games in Gamehistory?
        assertEquals(2, userService.getGameHistory("myrlxxn").size());
    }/*

    //Game sachen
    /*@Test
    void playGame() throws ApiRequestException{
        Move move = new Move(Square.E2, Square.E4);
        System.out.println("String Ausgabe von move lautet: " + move);
        GameRequest request = new GameRequest("spiel1", "my1", "my2", "3 min");
        Game gameRequest = new Game(request.getGameName(), request.getPlayer1(), request.getPlayer2(), null, null, false);
        System.out.println("aktuelle spielsituation: "+ gameRequest);

     }*/

    // Test: Timer - erfolgreich, aber warum ungenau um 1s sekunde manchmal?
    /*@Test
    void timer() throws InterruptedException {
    // Testfall 1: Timer starten formatiert ausgeben
        System.out.println("Testfall 1: Timer starten und formatiert Zeit ausgeben");
    ChessTimer chessTimer1 = new ChessTimer(60); // 1 Minute pro Spieler
        chessTimer1.startGame();
        Thread.sleep(5000);
        System.out.println("Formatted Time: " + chessTimer1.getFormattedTime());
        chessTimer1.stopTimer();
        System.out.println();

    // Testfall 2: Timer starten Zug wechseln und formatiert ausgeben
        System.out.println("Testfall 2: Timer starten, Zug wechseln und formatierte Zeit ausgeben");
    ChessTimer chessTimer2 = new ChessTimer(120); // 2 Minuten pro Spieler
        chessTimer2.startGame();
        Thread.sleep(5000);
        chessTimer2.switchTurn(); // Zugwechsel
        System.out.println("Formatted Time weiss: " + chessTimer2.getFormattedTime());
        Thread.sleep(5000);
        System.out.println("Formatted Time weiss: " + chessTimer2.getFormattedTime());
        chessTimer2.stopTimer();
        System.out.println("fertig");
        System.out.println();
}*/


    //Test: ChessClub - erstellen, joinen, members ausgeben, clubs eines users ausgeben
    /*@Test
    void createChessClub() throws ApiRequestException{
        //Registrierung der Nutzer
        SignUpRequest signUpRequest = new SignUpRequest
                ("myrlxxn", "1234","Marleen", "Gollan", "aleyna200342@gmail.com","2002-07-04");
        userService.registerUser(signUpRequest);
        SignUpRequest signUpRequest2 = new SignUpRequest
                ("myrlxxn2", "1234", "Marleen2", "Gollan2", "marleen.gollan@stud.uni-due.de", "2002-07-04");
        userService.registerUser(signUpRequest2);

        //Club erstellen
        System.out.println("Chessclubs vor creation: " + chessClubService.getAllChessClubNames());
        chessClubService.createChessClub("myrlxxn", "OGclub");
        chessClubService.createChessClub("myrlxxn2", "nochEinClub");
        System.out.println("Chessclubs nach creation: " + chessClubService.getAllChessClubNames());

        //Club joinen
        System.out.println("members direkt nach creation: " + chessClubService.getMembersForChessClub("OGclub") );
        chessClubService.joinChessClub("myrlxxn2", "OGclub");
        System.out.println("members nachdem ein user joined: " + chessClubService.getMembersForChessClub("OGclub") );

        //Liste von Clubs in dem ein User ist
        System.out.println("clubs in dem member mitglied ist: " + chessClubService.getChessClubsForUser("myrlxxn2"));
    }*/

    //Test: GameRequest - senden, annehmen, ablehen
    /*@Test
    void sendGameRequest() throws ApiRequestException{
        SignUpRequest signUpRequest = new SignUpRequest
                ("myrlxxn", "1234","Marleen", "Gollan", "aleyna200342@gmail.com","2002-07-04");
        userService.registerUser(signUpRequest);
        SignUpRequest signUpRequest2 = new SignUpRequest
                ("myrlxxn2", "1234", "Marleen2", "Gollan2", "marleen.gollan@stud.uni-due.de", "2002-07-04");
        userService.registerUser(signUpRequest2);
        SignUpRequest signUpRequest3 = new SignUpRequest
                ("Wango", "1234", "Marleen3", "Gollan3", "aleyna.erdogan@stud.uni-due.de", "2002-07-04");
        userService.registerUser(signUpRequest3);
        //anfrage senden
        gameService.sendGameRequest("Spiel1","myrlxxn", "myrlxxn2" );
        gameService.sendGameRequest("anderesSpiel","Wango", "myrlxxn2" );
        System.out.println("erhaltene Anfrage von: " +gameService.getPendingGameRequests("myrlxxn2"));

        //anfrage annehmen
        gameService.acceptGameRequest("Spiel1");
        gameService.acceptGameRequest("anderesSpiel");
        System.out.println("sollte leer sein: " +gameService.getPendingGameRequests("myrlxxn2"));
        System.out.println("meine Spiele: " + gameService.getAcceptedGameRequests("myrlxxn"));
        System.out.println("meine Spiele: " + gameService.getAcceptedGameRequests("myrlxxn2"));

        //anfrage ablehnen
        //gameService.rejectGameRequest("Spiel1");
        //System.out.println("abgelehnte Spiele: " + gameService.getGames());


        //System.out.println("angefragte Spiele: " + gameService.getPendingGames());
    }*/

    //Test: LeaderBoard
    /*@Test
    void getLeaderboard() throws ApiRequestException{
        //SignUpRequest signUpRequest = new SignUpRequest
                //("myrlxxn", "1234","Marleen", "Gollan", "aleyna200342@gmail.com","2002-07-04");
        //userService.registerUser(signUpRequest);
        //SignUpRequest signUpRequest2 = new SignUpRequest
                //("myrlxxn2", "1234", "Marleen2", "Gollan2", "marleen.gollan@stud.uni-due.de", "2002-07-04");
        //userService.registerUser(signUpRequest);
        //userService.registerUser(signUpRequest2);

        UserProfile profil1 = new UserProfile(1, "ultimategoat", "Aleyna", "Erdogan", "2003-04-15", "aleyna200342@gmail.com", 500, null );
        UserProfile profil2 = new UserProfile(2, "ultimateg", "Aleyna", "Erdogan", "2003-04-15", "aleyna200342@gmail.com", 800, null );
        UserProfile profil3 = new UserProfile(3, "ultimateg", "Aleyna", "Erdogan", "2003-04-15", "aleyna200342@gmail.com", 650, null );

        System.out.println(profil1.getPoints());

        System.out.println(profil2.getPoints());
        //userService.getLeaderboard();
        System.out.println("Rangliste der Spieler: "+ userService.getLeaderboard());
    }*/


    // Test: Freund entfernen UND löschen - Erfolgreich getestet
    /*@Test
    void removeFriend() throws ApiRequestException{
        SignUpRequest signUpRequest = new SignUpRequest
                ("myrlxxn", "1234","Marleen", "Gollan", "aleyna200342@gmail.com","2002-07-04");
        userService.registerUser(signUpRequest, null);
        SignUpRequest signUpRequest2 = new SignUpRequest
                ("myrlxxn2", "1234", "Marleen2", "Gollan2", "marleen.gollan@stud.uni-due.de", "2002-07-04");
        userService.registerUser(signUpRequest2, null);
        friendService.sendFriendRequest("myrlxxn2", "myrlxxn");
        //friendService.acceptFriendRequest("myrlxxn2", "myrlxxn");
        System.out.println("Die Freundesliste nach anfrage senden: "+ friendService.getPendingFriendList(signUpRequest.getUsername()));
        friendService.rejectFriendRequest("myrlxxn2", "myrlxxn");
        System.out.println("Die Freundesliste nach dem ablehnen: " + friendService.getPendingFriendList(signUpRequest.getUsername()));

    }*/

    // Test: FriendRequest ablehnen - Erfolgreich getestet
   /* @Test
    void rejectFriendRequest() throws ApiRequestException{
        SignUpRequest signUpRequest = new SignUpRequest
                ("myrlxxn", "1234","Marleen", "Gollan", "aleyna200342@gmail.com", LocalDate.of(2002, 7, 4));
        userService.registerUser(signUpRequest, null);
        SignUpRequest signUpRequest2 = new SignUpRequest
                ("myrlxxn2", "1234", "Marleen2", "Gollan2", "marleen.gollan@stud.uni-due.de", LocalDate.of(2002, 7, 4));
        userService.registerUser(signUpRequest2, null);
        friendService.sendFriendRequest("myrlxxn2", "myrlxxn");
        //friendService.rejectFriendRequest("myrlxxn2", "myrlxxn");
        friendService.acceptFriendRequest("myrlxxn2", "myrlxxn");
    }*/

    //Test: FriendRequest akzeptieren - Erfolgreich getestet
    /*@Test
    void acceptFriendRequest() throws  ApiRequestException {
        SignUpRequest signUpRequest = new SignUpRequest
                ("myrlxxn", "1234","Marleen", "Gollan", "mago4702@gmail.com", LocalDate.of(2002, 7, 4));
        userService.registerUser(signUpRequest, null);
        SignUpRequest signUpRequest2 = new SignUpRequest
                ("myrlxxn2", "1234", "Marleen2", "Gollan2", "marleen.gollan@stud.uni-due.de", LocalDate.of(2002, 7, 4));
        userService.registerUser(signUpRequest2, null);
        friendService.sendFriendRequest("myrlxxn2", "myrlxxn");
        friendService.acceptFriendRequest("myrlxxn2", "myrlxxn");
    }*/

    //Test: FriendRequest senden - Erfolgreich abgeschlossen
    /*@Test
    void sendFriendRequest() throws ApiRequestException {
        SignUpRequest signUpRequest = new SignUpRequest
                ("myrlxxn", "1234", "Marleen", "Gollan", "aleyna200342@gmail.com", LocalDate.of(2002, 7, 4));
        userService.registerUser(signUpRequest, null);
        SignUpRequest signUpRequest2 = new SignUpRequest
                ("myrlxxn2", "1234", "Marleen2", "Gollan2", "marleen.gollan@stud.uni-due.de", LocalDate.of(2002, 7, 4));
        userService.registerUser(signUpRequest2, null);
        friendService.sendFriendRequest("myrlxxn2", "myrlxxn");
    }
    */


//----------------------------------------------------------------------------------------------------------------------

    /* Test: Neues Hash and Salt password - Erfolgreich abgeschlossen
    @Test
    void password() throws ApiRequestException{
        SignUpRequest signUpRequest = new SignUpRequest
                ("myrlxxn", "1234", "Marleen", "Gollan", "marleen.gollan@stud.uni-due.de", "2002-07-04");
        User user = userService.registerUser(signUpRequest, null);
        System.out.println("!!! PASSWORD: " + user.getEncodedPassword() + " !!!");
        securityService.verifyPassword("1234", user.getEncodedPassword(), user.getUsername());
    }*/

    /* Test: Profil aufrufen - Erfolgreich abgeschlossen
    @Test
    void getYourProfile() throws ApiRequestException {
        SignUpRequest signUpRequest = new SignUpRequest
                ("myrlxxn", "1234", "Marleen", "Gollan", "mago4702@gmail.com", LocalDate.of(2002, 7, 4), null);
        MultipartFile profilePicture = null;
        User user = userService.registerUser(signUpRequest, profilePicture);
        userService.loginUser("myrlxxn", "1234");
        UserRequest userRequest = new UserRequest(user.getUsername(), userService.verifyTwoFactorAuthentication(user.getUsername() , "Super1"));
        userService.getUserProfile(userRequest);
    }*/

    /* Test: Doppelte registrierung (Username schon vorhanden) - Erfolgreich abgeschlossen
    @Test
    void doubleSignUp() throws ApiRequestException {
        SignUpRequest signUpRequest = new SignUpRequest
                ("myrlxxn", "1234", "Marleen", "Gollan", "marleen.gollan@stud.uni-due.de", LocalDate.of(2002, 7, 4), null);
        SignUpRequest signUpRequest2 = new SignUpRequest
                ("myrlxxn", "1234", "Marleen", "Gollan", "mago4702@gmail.com", LocalDate.of(2002, 7, 4), null);
        MultipartFile profilePicture = null;
        userService.registerUser(signUpRequest, profilePicture);
        userService.registerUser(signUpRequest2, profilePicture);
    }*/

    /* Test: Doppelte registrierung (Email schon vorhanden) - Erfolgreich abgeschlossen
    @Test
    void doubleSignUpName() throws ApiRequestException {
        SignUpRequest signUpRequest = new SignUpRequest
                ("myrlxxn2", "1234", "Marleen", "Gollan", "mago4702@gmail.com", LocalDate.of(2002, 7, 4), null);
        MultipartFile profilePicture = null;
        userService.registerUser(signUpRequest, profilePicture);
        userService.registerUser(signUpRequest, profilePicture);
    }*/

    //Test: 2FA verifizierung mit super code - Erfolgreich abgeschlossen
    /*@Test
    void verifyTwoFactorAuthentication() throws ApiRequestException {
        SignUpRequest signUpRequest = new SignUpRequest
                ("myrlxxn", "1234", "Marleen", "Gollan", "marleen.gollan@stud.uni-due.de", "2002-07-04");
        MultipartFile profilePicture = null;
        User user = userService.registerUser(signUpRequest, profilePicture);
        userService.loginUser("myrlxxn", "1234");
        if (userService.verifyTwoFactorAuthentication(user.getUsername() , "Super1")){
            System.out.println("2FA klappt");
        }
    }*/

    /* Test: Login mit richtigen anmeldedaten - Erfolgreich abgeschlossen
    @Test
    void registerAndLogin() throws ApiRequestException {
        SignUpRequest signUpRequest = new SignUpRequest
                ("myrlxxn", "1234", "Marleen", "Gollan", "mago4702@gmail.com", LocalDate.of(2002, 7, 4), null);
        MultipartFile profilePicture = null;
        userService.registerUser(signUpRequest, profilePicture);
        userService.loginUser("myrlxxn", "1234");
    }*/

    //Test: Registrierung (ohne Profilbild) - Erfolgreich abgeschlossen
    /*@Test
    void testRegisterUser() throws ApiRequestException {
        SignUpRequest signUpRequest = new SignUpRequest
                ("myrlxxn", "Hallo@1234","Marleen", "Gollan", "mago4702@gmail.com","2001-09-27");
        userService.registerUser(signUpRequest, null);
    }*/

    /* Test: Registrierung - Erfolgreich abgeschlossen (vor der Passwortspeicherung als Hash)
    @Test
    void testSendTwoFactorCode() {
        // Erstellen eines Testbenutzers
        User user = new User();
        user.setFirstName("Test");
        user.setLastName("User");
        user.setEmail("mago4702@gmail.com");
        user.setPassword("TestPassword");
        user.setPoints(500);
        user.setProfilePicture(null); // Stellen Sie sicher, dass dies auf eine gültige Datei verweist

        userService.performTwoFactorAuthentication(user);
    }*/


}