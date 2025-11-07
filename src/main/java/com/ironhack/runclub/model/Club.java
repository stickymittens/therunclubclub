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

    @ManyToOne
    @JoinColumn(name = "club_owner_id")
    private User clubOwner;

    //based on existing events
//    private int totalRunners;
//    private int totalDistance;
//    private double clubScore;


    //getters and setters
    public Long getId() {
        return id;
    }

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

    public User getClubOwner() {
        return clubOwner;
    }

    public void setClubOwner(User clubOwner) {
        this.clubOwner = clubOwner;
    }


    //constructors
    public Club(String clubName, String clubDescription) {
        this.clubName = clubName;
    }

    public Club(){}
}
