package com.example.backend.service;

import com.example.backend.DTO.GameHistory;
import com.example.backend.DTO.LeaderboardDTO;
import com.example.backend.DTO.SignUpRequest;
import com.example.backend.DTO.UserProfile;
import com.example.backend.SecurityService;
import com.example.backend.entity.Game;
import com.example.backend.entity.SessionToken;
import com.example.backend.exception.ApiRequestException;
import com.example.backend.repository.GameRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Sort;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final SecurityService securityService;
    private final GameRepository gameRepository;


    public UserService(UserRepository userRepository, SecurityService securityService,
                       GameRepository gameRepository) {
        this.userRepository = userRepository;
        this.securityService = securityService;
        this.gameRepository = gameRepository;
    }

    public User registerUser(SignUpRequest signUpData) throws ApiRequestException {
        User emailcheck = userRepository.findByEmail(signUpData.getEmail());
        User usernamecheck = userRepository.findByUsername(signUpData.getUsername());
        if(emailcheck != null){
            throw new ApiRequestException("Email schon vorhanden!");
        }
        if (usernamecheck != null){
            throw new ApiRequestException("Username schon vergeben!");
        }
        //Passwort hash und salt
        String[] hashAndSalt = securityService.hashAndSaltPassword(signUpData.getPassword(), securityService.generateSalt());
        String encodedPassword = hashAndSalt[0];
        String salt = hashAndSalt[1];
        // Speichere den Benutzer in der Datenbank
        User newUser = new User(signUpData.getUsername(), signUpData.getFirstname(), signUpData.getLastname(),
                signUpData.getBirthdate(), signUpData.getEmail(), encodedPassword, salt);
        newUser.setProfilePicture("assets/ProfilePictures/defaultprofilepicture.png");
        userRepository.save(newUser);
        return userRepository.findByUsername(newUser.getUsername());
    }

    public boolean loginUser(String inputName, String inputPassword) throws ApiRequestException {
        User storedUser = userRepository.findByUsername(inputName); // User anhand Username in DB finden
        if (storedUser == null) { // Überprüfen ob User mit Username existiert
            throw new ApiRequestException("Username oder Passwort ist falsch!");
        }
        // String storedPassword = storedUser.getEncodedPassword();
        String storedPasswordHash = storedUser.getEncodedPassword();
        if (securityService.verifyPassword(inputPassword, storedPasswordHash, storedUser.getSalt())) { // Passwort überprüfen
        //if (storedPassword.equals(inputPassword)) {
            return performTwoFactorAuthentication(storedUser);
        } else {
            throw new ApiRequestException("Username oder Passwort ist falsch!");
        }
    }

    // Senden des 2FA-Codes
    public boolean performTwoFactorAuthentication(User user){
        return securityService.performTwoFactorAuthentication(user);
    }
    // Überprüfung des eingegebenen Codes
    public boolean verifyTwoFactorAuthentication(String username, String inputCode) {
        return securityService.verifyAuthCode(userRepository.findByUsername(username), inputCode);
    }

    public UserProfile getUserProfile(String username) {
        // Wenn der Token gültig ist werden die Profildaten übergeben
        if (securityService.isValidToken(username)){
            User user = userRepository.findByUsername(username);
            //List<String> chessClubs = getChessClubsForUser(username);
            return new UserProfile(user.getUserID(), user.getUsername(), user.getFirstName(),
                    user.getLastName(), user.getBirthDate(), user.getEmail(), user.getPoints(), user.getProfilePicture()/*,chessClubs*/);

        } else return null;
    }

    public boolean setProfilePicture(String username, MultipartFile profilePicture) throws ApiRequestException, IOException {
        User user = userRepository.findByUsername(username);
        if (profilePicture == null || user == null) {
             throw new ApiRequestException("Data is not valid!");
        }
        // Speichere das Profilbild
        if (!Objects.equals(profilePicture.getOriginalFilename(), "assets/ProfilePictures/defaultprofilepicture.png")) {
            // Definiert den Zielpfad, an dem die hochgeladene Datei gespeichert wird
            String originalFileName = profilePicture.getOriginalFilename();
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));

            // Generiert einen eindeutigen Dateinamen mit Hilfe einer UUID und dem ursprünglichen Dateinamen
            String uploadedFileName = UUID.randomUUID() + "_" + user.getUserID() + fileExtension;

            // Im Entwicklungsmodus:
            Path targetPath = Paths.get("frontend", "src", "assets", "ProfilePictures", uploadedFileName);

            // Im Produktionsmodus (wenn das Backend und Frontend getrennt sind):
            // Passe den Pfad an, um auf den entsprechenden Assets-Ordner im Produktions-Build deiner Angular-Anwendung zu verweisen.
            // Path targetPath = Paths.get("Pfad zum Produktions-Assets-Ordner", "profilePictures", uploadedFileName);

            // Kopiert den Inhalt der hochgeladenen Datei in den Zielpfad
            try (InputStream inputStream = profilePicture.getInputStream()) {
                Files.createDirectories(targetPath.getParent());

                if (Files.exists(targetPath)) {
                    Files.delete(targetPath);
                }
                System.out.println("Copying to: " + targetPath);
                Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new ApiRequestException("Error copying profile picture file: " + e.getMessage());
            }
            System.out.println("Pfad: "+targetPath.toString());

            // Setzt ProfilePicture auf den Dateipfad, um auf das hochgeladene Profilbild zu verweisen
            // Hier wird es als relativer Pfad im Frontend gespeichert.
            user.setProfilePicture("assets/ProfilePictures/" + uploadedFileName);
            userRepository.save(user);
            return true;
        } else {
            user.setProfilePicture("assets/ProfilePictures/defaultprofilepicture.png");
            userRepository.save(user);
        }
        return false;
    }

    /*public void uploadPicture(String username, byte[] profilePicture){
        User user = userRepository.findByUsername(username);
        if(profilePicture != null) {
            user.setProfilePicture(null);
            user.setProfilePicture(profilePicture);
            userRepository.save(user);
        }
    }
    public String getPicture(String username){
        User user = userRepository.findByUsername(username);
        return convertToBase64(user.getProfilePicture());
    }
    private String convertToBase64(byte[] profilePicture) {

        return Base64.getEncoder().encodeToString(profilePicture);
    }*/

    public SessionToken getSessionTokenByUsername(String username){
        if (securityService.isValidToken(username)){
            return securityService.getToken(username);
        } else return null;
    }
    public boolean getIsPrivateByUsername(String username){
        return userRepository.findByUsername(username).getIsPrivate();
    }
    public boolean usernameExists(String username){
        return userRepository.findByUsername(username) != null;
    }
    public List<User> getUsers() {
        return userRepository.findAll();
    }
    public List<SessionToken> getTokens() {return securityService.getTokens();}

    public boolean updatePrivacy(String username, boolean isPrivate){
        User user = userRepository.findByUsername(username);
        if (user != null) {
            user.setPrivate(isPrivate);
            userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }
    public User getUserByName(String username) {
        User userer = new User();
        List<User> users = userRepository.findAll(Sort.by(Sort.Direction.DESC, "points"));
        for(User user :users){
            if(user.getUsername().equals(username)){
                userer = user;
            }
        }
        return userer;
    }
    public List<LeaderboardDTO> getLeaderboard() {
        List<User> users = userRepository.findAll(Sort.by(Sort.Direction.DESC, "points"));
        List<LeaderboardDTO> leaderboard = new ArrayList<>();
        for (User user : users) {
            if(!user.getUsername().equals("Bot Yasir")) {
                leaderboard.add(new LeaderboardDTO(user.getUsername(), user.getPoints()));
            }
        }
        return leaderboard;
    }

    public String getProfilePicturePath(String username) throws ApiRequestException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new ApiRequestException("User not found");
        }
        return user.getProfilePicture();
    }

            /*public List<String> getChessClubsForUser(String member) {
        List<ChessClub> clubs = chessClubRepository.findByMembers(member);
        List<String> clubNames = new ArrayList<>();
        for(ChessClub club: clubs){
            clubNames.add(club.getClubName());
        }
        return clubNames;
    }*/

    public List<GameHistory> getGameHistory(String username){
        if(userRepository.findByUsername(username)!=null){
            // Erstes Spiel in der Liste ist das zuletzt abgeschlossene
            List<Game> allCompletedGames = gameRepository.findAllCompletedGamesForUser(username);
            // Nehme die ersten drei Spiele, wenn vorhanden
            int numberOfGamesToRetrieve = Math.min(3, allCompletedGames.size());
            List<Game> games = allCompletedGames.subList(0, numberOfGamesToRetrieve);

            // Für alle drei Games Gamehistory erstellen und in List speichern
            List<GameHistory> gameHistory = new ArrayList<GameHistory>();
            String enemyName;
            for(Game game:games){
                // Wenn User Player1 ist, ist der Gegner Player2 und andersrum
                if(game.getPlayer1().equals(username)){
                    enemyName = game.getPlayer2();
                } else { enemyName = game.getPlayer1();}
                // Gamehistory zur Liste hinzufügen
                gameHistory.add(new GameHistory(username, enemyName, userRepository.findByUsername(enemyName).getPoints(),
                        userRepository.findByUsername(username).getPoints(), game.getResult(), game.getCompletionDate(),
                        game.getId(), game.getPlayer1(),game.getPlayer2()));
            }
            return gameHistory;
        } else return null;
    }

    public Boolean logout(String username){
        if(userRepository.findByUsername(username)!=null){
            if(securityService.getToken(username)!=null){
                securityService.deleteToken(username);
                return true;
            }
        }
        return false;
    }

    public void createTestGame(){
        List<String> temp = new ArrayList<>(Arrays.asList("e4", "e5", "d4", "exd4", "Qxd4", "Nc6", "Qe3", "Nf6", "Nc3", "Bb4", "Bd2", "O-O", "O-O-O", "Re8", "Qq3", "Rxe4", "a3", "1-0"));
        Game gametest= new Game("test", "WangoNummerEins", "Yasir29", null, temp, true, 0);
        gametest.setResult("1-0");
        gametest.setCompletionDate(new Date());
        gameRepository.save(gametest);

        Game gametest2= new Game("test2", "Yasir29", "WangoNummerZwei", null, null, true, 0);
        gametest2.setResult("0-1");
        gametest2.setCompletionDate(new Date());
        gameRepository.save(gametest2);

        Game gametest3= new Game("test3", "Yasir29", "WangoNummerEins", null, null, true, 0);
        gametest3.setResult("1/2-1/2");
        gametest3.setCompletionDate(new Date());
        gameRepository.save(gametest3);
    }
}

