package com.ironhack.runclub.model;

import com.ironhack.runclub.enums.CitiesEnum;
import jakarta.persistence.Entity;

@Entity
public class ClubOwner extends User {
    //constructors
    public ClubOwner(String userName, String email, CitiesEnum city) {
        super(userName, email, city);
    }

    public ClubOwner(){}
}
