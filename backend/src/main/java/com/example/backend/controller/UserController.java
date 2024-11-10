package com.example.backend.controller;

import com.example.backend.DTO.*;
import com.example.backend.entity.SessionToken;
import com.example.backend.entity.User;
import com.example.backend.exception.ApiRequestException;
import com.example.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

// Kennzeichnet eine Klasse als Controller, die HTTP-Anfragen bearbeitet und JSON-Antworten zurückgibt
@RestController
// Ermöglicht Cross-Origin Resource Sharing (CORS), um AJAX-Anfragen von anderen Domänen zuzulassen
@CrossOrigin
public class UserController {

    // Durch Dependency Injection bereitgestellt (automatisch von Springboot)
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Anfrage zur Registrierung
    @PostMapping("/registerUser")
    public ResponseEntity<Void> registerUser(@RequestBody SignUpRequest signUpData) throws ApiRequestException {
        if (userService.registerUser(signUpData) != null) {
            // Die Registrierung war erfolgreich, also wird ein "200 OK"-Statuscode zurückgegeben
            return ResponseEntity.ok().build();
        } else {
            // Die Registrierung war nicht erfolgreich, also wird ein HTTP-Statuscode (400 Bad Request) zurückgegeben
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Anfrage zum Login und Senden des 2FA-Codes
   @PostMapping("/login")
    public ResponseEntity<Void> loginUser(@RequestBody UserLoginRequest userLoginRequest) throws ApiRequestException {
        if (userService.loginUser(userLoginRequest.getUsername(), userLoginRequest.getPassword())) {
            // Das Login war erfolgreich, also wird ein "200 OK"-Statuscode zurückgegeben
            return ResponseEntity.ok().build();
        } else {
            // Das Login war nicht erfolgreich, also wird ein HTTP-Statuscode (401 Unauthorized) zurückgegeben
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    // Anfrage zur Überprüfung des 2FA-Codes, übergibt Sitzungstoken
    @PostMapping("/2FA")
    public ResponseEntity<Void> verifyAuth(@RequestBody Map<String, String> request) throws ApiRequestException  {
        String username = request.get("username");
        String inputCode = request.get("inputCode");
        if (userService.verifyTwoFactorAuthentication(username, inputCode)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    // Anfrage zur Änderung der Privacy-Einstellung für Friendlist
   @PostMapping("/updatePrivacy")
    public ResponseEntity<Void> updatePrivacy(@RequestBody PrivacyUpdateRequest privacyUpdateRequest) {
        if(userService.updatePrivacy(privacyUpdateRequest.getUsername(),privacyUpdateRequest.isPrivate())){
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Daten des Profils abrufen
    @GetMapping("/profile/{username}")
    public ResponseEntity<UserProfile> getUserProfile(@PathVariable String username){
        // Im übergebenen String sind "", deshalb löschen
        if (userService.getUserProfile(username) != null){
            String firstname = userService.getUserProfile(username).getFirstName();
            return ResponseEntity.ok(userService.getUserProfile(username));
        } else {
            // Senden eines Fehlerstatus, wenn das Sitzungstoken ungültig ist
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    // Profilbildpfad übergeben
    @GetMapping(value = "/getProfilePicture/{username}")
    public ResponseEntity<String> getProfilePicture(@PathVariable String username) throws ApiRequestException{
        try {
            String profilePicturePath = userService.getProfilePicturePath(username);
            // Backslashes durch Schrägstriche erstezen (sonst error weil \ ist escape-Zeichen in JSON)
            String pathWithSlashes = profilePicturePath.replace("\\", "/");
            // Extra Anführungzeichen wegen JSON
            String pathWithQuotes = "\"" + pathWithSlashes + "\"";
            return ResponseEntity.ok(pathWithQuotes);
        } catch (ApiRequestException e) {
            return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).build();
        }
    }

    // Profilbild ändern
    @PostMapping("/setProfilePicture")
    public ResponseEntity<Void> setProfilePicture(@RequestParam String username, @RequestPart MultipartFile profileImage) throws IOException, ApiRequestException {
        if (userService.setProfilePicture(username, profileImage)){
            return ResponseEntity.ok().build();
        } else return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

   /* @PostMapping("uploadPicture")
    public ResponseEntity<String> uploadPicture(@RequestParam String username, @RequestBody MultipartFile profilePic) throws IOException {
        byte[] arr = profilePic.getBytes();
        userService.uploadPicture(username, arr);
        return ResponseEntity.ok("Profilbild wurde hochgeladen!");
    }

    @GetMapping("/getPicture")
    public ResponseEntity<String> getPicture(@RequestParam String username){
        String profilePicture = userService.getPicture(username);
        return ResponseEntity.ok(profilePicture);
    }*/

    // Session-Token eines Users übergeben
    @GetMapping("/getSessionToken")
    public ResponseEntity<SessionToken> getSessionToken(@RequestParam String username){
        if(userService.getSessionTokenByUsername(username) != null){
            return ResponseEntity.ok(userService.getSessionTokenByUsername(username));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Privacy-Einstellung der Friendlist übergeben
    @GetMapping("/getIsPrivate/{username}")
    public ResponseEntity<Boolean> getIsPrivate(@PathVariable String username){
        if (userService.usernameExists(username)) {
            return ResponseEntity.ok(userService.getIsPrivateByUsername(username));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }

    // User anhand des Usernames übergeben
    @GetMapping("/getUserByName/{username}")
    public ResponseEntity<User> getUserByName(@PathVariable String username){
        if(userService.usernameExists(username)){
            return ResponseEntity.ok(userService.getUserByName(username));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Leaderbord-Daten übergeben
    @GetMapping("/getLeaderboard")
    public ResponseEntity<List<LeaderboardDTO>> getLeaderboard() {
        try {
            List<LeaderboardDTO> leaderboard = userService.getLeaderboard();
            return ResponseEntity.ok(leaderboard);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Die drei zuletzt abgeschlossenen Game-Daten eines Users übergeben
    @GetMapping("/getGameHistory/{username}")
    public ResponseEntity<List<GameHistory>> getGameHistory(@PathVariable String username){
        if(userService.getGameHistory(username)!=null){
            List<GameHistory> gameHistory = userService.getGameHistory(username);
            return ResponseEntity.ok(gameHistory);
        } else return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody String loggesUser){
        if(userService.logout(loggesUser)){
            return ResponseEntity.ok().build();
        } else return  ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    //----------------------DATENBANKTESTS---------------------------------------------------------

    // Alle registrierten User übergeben
    @GetMapping("/getUsers")
    public List<User> getUsers(){
        return userService.getUsers();
    }

    // Alle gespeicherten Sessiontokens übergeben
    @GetMapping("/getTokens")
    public List<SessionToken> getTokens(){return userService.getTokens();}
}

