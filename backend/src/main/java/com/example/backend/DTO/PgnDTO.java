package com.example.backend.DTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ElementCollection;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Getter
@Setter
public class PgnDTO {

    @Column(nullable = false)
    private String date;
    @Column(nullable = false)
    private String white;
    @Column(nullable = false)
    private String black;
    @Column(nullable = false)
    private String result;
    @Column(nullable = false)
    private String moves = "";

    public PgnDTO(){

    }
}
