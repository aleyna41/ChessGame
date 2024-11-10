package com.example.backend.repository;

import com.example.backend.entity.GameRequestStatus;
import jakarta.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.backend.entity.Game;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.yaml.snakeyaml.events.Event;

import java.awt.print.Pageable;
import java.util.List;


@Repository
public interface GameRepository extends JpaRepository<Game, Long>{

    List<Game> findAll();
    boolean existsByGameName(String gameName);
    boolean existsByPlayer1AndPlayer2AndResult(String player1, String player2, String Result);
    List<Game> findByStatus(GameRequestStatus gameRequestStatus);
    Game findByGameName(String gameName);
    List<Game> findByPlayer2AndStatus(String player2, GameRequestStatus gameRequestStatus);
    List<Game> findByPlayer1AndStatus(String player1, GameRequestStatus gameRequestStatus);
    Game findGameById(Long id);

    // GameHistory
    @Query("SELECT g FROM Game g WHERE (g.player1 = :username OR g.player2 = :username) AND g.result IS NOT NULL ORDER BY g.completionDate DESC")
    List<Game> findAllCompletedGamesForUser(@Param("username") String username);

    @Query("SELECT g.gameName FROM Game g")
    List<String> findAllGameNames();
    @Query("SELECT g.gameName FROM Game g WHERE g.streaming = TRUE AND g.result IS NULL")
    List<String> findStreams();

    List<Game> findByPlayer2AndStatusAndResult(String player, GameRequestStatus gameRequestStatus, String result);

    List<Game> findByPlayer1AndStatusAndResult(String player, GameRequestStatus gameRequestStatus, String result);
}




