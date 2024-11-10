package com.example.backend.repository;

import com.example.backend.entity.ChatHistory;
import com.example.backend.entity.ClubChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClubChatRepository extends JpaRepository<ClubChat, Long> {
    @Query("SELECT cc FROM ClubChat cc WHERE cc.chessClub.clubName = :club")
    ClubChat getClubChat(@Param("club") String club);
}
