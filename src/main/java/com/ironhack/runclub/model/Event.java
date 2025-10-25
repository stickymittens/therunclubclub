package com.ironhack.runclub.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull (message = "Date must be set")
    private OffsetDateTime dateTime;

    @NotNull (message = "Meeting point must be set")
    private String meetingPoint;

    @NotNull (message = "Distance must be set")
    private double distance;

    @NotNull (message = "Pace must be set")
    private double pace;

    private String eventDescription;

    @ManyToOne
    @JoinColumn(name = "club_id")
    private Club club;


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


    //constructors
    public Event(OffsetDateTime dateTime, String meetingPoint, double distance, double pace, String eventDescription, Club club) {
        this.dateTime = dateTime;
        this.meetingPoint = meetingPoint;
        this.distance = distance;
        this.pace = pace;
        this.eventDescription = eventDescription;
        this.club = club;
    }

    public Event(){}
}
