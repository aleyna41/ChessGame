package com.example.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Transient;
import lombok.Getter;
import org.springframework.data.annotation.Id;

import java.util.Timer;
import java.util.TimerTask;

@Getter
@Entity
public class ChessTimer {


    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Transient
    private java.util.Timer timer;
    @Getter
    private String formattedTime;

    private int whiteTimer;

    private int blackTimer;

    private boolean isWhiteTurn;


    public ChessTimer(int secondsLeft) {
        this.whiteTimer = secondsLeft;
        this.blackTimer = secondsLeft;
        this.isWhiteTurn = true;
        this.timer = new Timer();
    }

    public ChessTimer() {
    }

    public void startGame() {
        startTimer();
    }


    public void switchTurn() {
        isWhiteTurn = !isWhiteTurn;
    }


    private void startTimer() {

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (whiteTimer == 1 || blackTimer == 1) {
                    timer.cancel();
                }
                if (isWhiteTurn) {
                    whiteTimer--;
                } else {
                    blackTimer--;
                }
            }

        };
        timer.scheduleAtFixedRate(task, 0, 1000);
    }


    public void stopTimer() {
        timer.cancel();
    }

}