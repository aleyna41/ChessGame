package com.example.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.backend.entity.User;

import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<User, Long>
{
     User findByEmail(String email);
     User findByUsername(String username);
     Boolean existsByUsername(String username);

     List<User> findAll();
}