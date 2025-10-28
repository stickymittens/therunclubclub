package com.ironhack.runclub.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public class Club {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull (message = "Name must be set")
    private String clubName;

    private String clubDescription;

    //based on existing events
    private int totalRunners;
    private int totalDistance;
    private double clubScore;


    //getters and setters
    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getClubDescription() {
        return clubDescription;
    }

    public void setClubDescription(String clubDescription) {
        this.clubDescription = clubDescription;
    }

    public int getTotalRunners() {
        return totalRunners;
    }

    public void setTotalRunners(int totalRunners) {
        this.totalRunners = totalRunners;
    }

    public int getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(int totalDistance) {
        this.totalDistance = totalDistance;
    }

    public double getClubScore() {
        return clubScore;
    }

    public void setClubScore(double clubScore) {
        this.clubScore = clubScore;
    }


    //constructors
    public Club(String clubName, String clubDescription) {
        this.clubName = clubName;
    }

    public Club(){}
}
