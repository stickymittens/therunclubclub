package com.ironhack.runclub.model;

import jakarta.persistence.ManyToMany;
import java.time.OffsetTime;
import java.util.List;

public class UserFields extends User{
    private double preferredPace;
    private int preferredDistance;
    private OffsetTime preferredStartTime;

    @ManyToMany(mappedBy = "users")
    private List<Event> signedUpEvents;


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

    public List<Event> getSignedUpEvents() {
        return signedUpEvents;
    }

    public void setSignedUpEvents(List<Event> signedUpEvents) {
        this.signedUpEvents = signedUpEvents;
    }


    //constructors
    public UserFields(String name, String username, String password, double preferredPace, int preferredDistance, OffsetTime preferredStartTime, List<Event> signedUpEvents) {
        super(name, username, password);
        this.preferredPace = preferredPace;
        this.preferredDistance = preferredDistance;
        this.preferredStartTime = preferredStartTime;
        this.signedUpEvents = signedUpEvents;
    }

    public UserFields(){}
}
