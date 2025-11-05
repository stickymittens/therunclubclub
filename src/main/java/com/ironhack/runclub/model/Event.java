package com.ironhack.runclub.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ironhack.runclub.enums.CitiesEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;
import java.util.List;

@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull (message = "Date must be set")
    private OffsetDateTime dateTime;

    @NotNull (message = "Meeting point must be set")
    private String meetingPoint;

    @NotNull (message = "City must be set")
    @Enumerated(EnumType.STRING)
    private CitiesEnum city;

    @NotNull (message = "Distance must be set")
    private double distance;

    @NotNull (message = "Pace must be set")
    private double pace;

    private String eventDescription;

    @ManyToOne
    @JoinColumn(name = "club_id")
    private Club club;

    @ManyToOne
    @JoinColumn(name = "event_owner_id")
    private User eventOwner;

    @ManyToMany
    @JoinTable(
            name = "event_user",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "user_username")
    )
    @JsonIgnoreProperties({"signedUpEvents"})
    private List<User> signedUpUsers;


    //getters and setters
    public Long getId() {
        return id;
    }

    public OffsetDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(OffsetDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getMeetingPoint() {
        return meetingPoint;
    }

    public void setMeetingPoint(String meetingPoint) {
        this.meetingPoint = meetingPoint;
    }

    public CitiesEnum getCity() {
        return city;
    }

    public void setCity(CitiesEnum city) {
        this.city = city;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getPace() {
        return pace;
    }

    public void setPace(double pace) {
        this.pace = pace;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    public User getEventOwner() {
        return eventOwner;
    }

    public void setEventOwner(User eventOwner) {
        this.eventOwner = eventOwner;
    }

    public List<User> getSignedUpUsers() {
        return signedUpUsers;
    }

    public void setSignedUpUsers(List<User> signedUpUsers) {
        this.signedUpUsers = signedUpUsers;
    }

    //constructors
    public Event(OffsetDateTime dateTime, String meetingPoint, CitiesEnum city, double distance, double pace, String eventDescription, Club club) {
        this.dateTime = dateTime;
        this.meetingPoint = meetingPoint;
        this.city = city;
        this.distance = distance;
        this.pace = pace;
        this.eventDescription = eventDescription;
        this.club = club;
    }

    public Event(){}
}
