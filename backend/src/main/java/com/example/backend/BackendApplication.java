package com.example.backend;

import com.example.backend.DTO.SignUpRequest;
import com.example.backend.DTO.UserProfile;
import com.example.backend.entity.User;
import com.example.backend.exception.ApiRequestException;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDate;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }


    @Bean
    CommandLineRunner init(UserService userService, FriendService friendService, ChessClubService chessClubService, ChessPuzzleService chessPuzzleService, GameService gameService){

        return args -> {
           SignUpRequest signUpRequest = new SignUpRequest("Yasir29", "Hallo@1234", "Yasir", "Tanis","mago4702@gmail.com", "2001-02-02");
           SignUpRequest signUpRequest2 = new SignUpRequest("WangoNummerEins", "Hallo@1234", "Tom", "Kersken","tom.kersken@gmail.com", "2001-09-27");
           SignUpRequest signUpRequest3 = new SignUpRequest("WangoNummerZwei", "Hallo@1234", "Woah", "Woahsen","tom.kersken+test@gmail.com", "2001-02-02");
           SignUpRequest signUpRequest4 = new SignUpRequest("Bot Yasir", "Hallo@1234", "Bot", "Bot","tom.kersken+bot@gmail.com", "2001-02-02");
            try {
                userService.registerUser(signUpRequest);
                userService.registerUser(signUpRequest2);
                userService.registerUser(signUpRequest3);
                userService.registerUser(signUpRequest4);
                friendService.sendFriendRequest("WangoNummerEins", "WangoNummerZwei");
                friendService.sendFriendRequest("Yasir29", "WangoNummerEins");
                friendService.acceptFriendRequest("Yasir29", "WangoNummerEins");
                //friendService.acceptFriendRequest("WangoNummerZwei", "WangoNummerEins"); //das gehardcoded erzeugt einen error
                //friendService.rejectFriendRequest("WangoNummerEins","WangoNummerZwei");
                //chessClubService.createChessClub("test");
                // chessClubService.createChessClub("test29");
                chessClubService.getAllChessClubNames();
                chessPuzzleService.increasePassed("WangoNummerEins", "001gi");
                chessPuzzleService.increasePassed("WangoNummerEins", "000aY");
                chessPuzzleService.increasePassed("WangoNummerEins", "000rO");
//              gameService.sendGameRequest("Hallo", "WangoNummerEins", "Yasir29", 3);
//              gameService.acceptGameRequest("Hallo");
                userService.createTestGame();
            } catch (ApiRequestException e) {
                throw new RuntimeException(e);
            }
        };

    }


}
