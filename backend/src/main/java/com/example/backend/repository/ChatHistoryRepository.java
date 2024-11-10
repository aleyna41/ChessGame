package com.example.backend.repository;

import com.example.backend.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatHistoryRepository extends JpaRepository<ChatHistory, Long> {

    @Query("SELECT c FROM ChatHistory c WHERE c.chat = :privateChat")
    ChatHistory getChatHistory(@Param("privateChat") PrivateChat privatechat);

    @Query("SELECT ch FROM ChatHistory ch WHERE ch.chat = :club")
    ChatHistory getClubHistory(@Param("club") ClubChat club);

    @Query("SELECT gh FROM ChatHistory gh WHERE gh.chat = :group")
    ChatHistory getGroupHistory(@Param("group")GroupChat group);
}
