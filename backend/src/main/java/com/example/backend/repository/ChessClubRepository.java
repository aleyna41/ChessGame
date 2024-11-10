package com.example.backend.repository;

import com.example.backend.entity.ChessClub;
import com.example.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChessClubRepository extends JpaRepository<ChessClub, Long> {

    List<ChessClub> findAll();

    ChessClub findByClubName(String clubName);

    boolean existsByClubName(String clubName);


    List<ChessClub> findByMembers(String member);

    @Query("SELECT c.members FROM ChessClub c WHERE c.clubName = :clubName")
    List<String> findMembersByClubName(@Param("clubName") String clubName);

}
