package com.example.backend.repository;

import com.example.backend.entity.SessionToken;
import com.example.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SessionTokenRepository extends JpaRepository<SessionToken, Long> {
    SessionToken findByUsername(String username);
    SessionToken findByTokenId(Long id);
    List<SessionToken> findAll();
}
