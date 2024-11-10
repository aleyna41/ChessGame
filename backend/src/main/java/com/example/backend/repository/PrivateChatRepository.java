package com.example.backend.repository;

import com.example.backend.entity.Friend;
import com.example.backend.entity.FriendStatus;
import com.example.backend.entity.PrivateChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

// Für alle Chats mit id und so aber ohne den inhalt
@Repository
public interface PrivateChatRepository extends JpaRepository<PrivateChat, Long> {

    @Query("SELECT c FROM PrivateChat c WHERE c.creatorName = :creator AND c.friendName = :friend")
    PrivateChat getPrivateChat(@Param("creator") String creator, @Param("friend") String friend);

    @Query("SELECT c From PrivateChat c WHERE c.creatorName = :username OR c.friendName = :username")
    List<PrivateChat> getPrivateChats(@Param("username") String username);
}
