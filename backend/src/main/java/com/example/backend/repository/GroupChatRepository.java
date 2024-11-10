package com.example.backend.repository;

import com.example.backend.entity.GroupChat;
import com.example.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GroupChatRepository extends JpaRepository<GroupChat,Long> {
    @Query("SELECT gc FROM GroupChat gc WHERE :username MEMBER OF gc.members")
    List<GroupChat> getGroupChats(@Param("username") String username);

    @Query("SELECT gc FROM GroupChat gc WHERE gc.groupName = :name AND gc.creatorName = :creator")
    GroupChat getGroupChat(@Param("name") String name, @Param("creator") String creator);

    @Query("SELECT gc FROM GroupChat gc WHERE gc.groupName = :name AND :username MEMBER OF gc.members")
    GroupChat getGroupChat2(@Param("name") String name, @Param("username") String username);
}
