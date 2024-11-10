package com.example.backend.repository;

import com.example.backend.entity.ChessPuzzle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChessPuzzleRepository extends JpaRepository<ChessPuzzle, String> {
    ChessPuzzle findChessPuzzleByThemes(String themes);

    ChessPuzzle findChessPuzzleByPuzzleId(String puzzleId);

    List<ChessPuzzle> findAll();

}
