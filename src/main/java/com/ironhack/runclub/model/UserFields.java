package com.ironhack.runclub.model;

import jakarta.persistence.ManyToMany;
import java.time.OffsetTime;
import java.util.List;

public class UserFields extends User{
    private double preferredPace;
    private int preferredDistance;
    private OffsetTime preferredStartTime;

//    @ManyToMany(mappedBy = "users")
//    private List<Event> signedUpEvents;


    //getters and setters
    public double getPreferredPace() {
        return preferredPace;
    }

    public void setPreferredPace(double preferredPace) {
        this.preferredPace = preferredPace;
    }

    public int getPreferredDistance() {
        return preferredDistance;
    }

    public void setPreferredDistance(int preferredDistance) {
        this.preferredDistance = preferredDistance;
    }

    public OffsetTime getPreferredStartTime() {
        return preferredStartTime;
    }

    public void setPreferredStartTime(OffsetTime preferredStartTime) {
        this.preferredStartTime = preferredStartTime;
    }


    //constructors
    public UserFields(String name, String username, String password, double preferredPace, int preferredDistance, OffsetTime preferredStartTime) {
        super(name, username, password);
        this.preferredPace = preferredPace;
        this.preferredDistance = preferredDistance;
        this.preferredStartTime = preferredStartTime;
    }

    public UserFields(){}
}
