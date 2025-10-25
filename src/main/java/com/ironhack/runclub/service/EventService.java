package com.ironhack.runclub.service;

import com.fasterxml.jackson.datatype.jsr310.ser.OffsetTimeSerializer;
import com.ironhack.runclub.exceptions.NoItemWithThisId;
import com.ironhack.runclub.exceptions.NoUpcomingEvents;
import com.ironhack.runclub.model.Event;
import com.ironhack.runclub.respository.EventRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {
    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository){
        this.eventRepository = eventRepository;
    }

    //CRUD
    //create
    public Event createEvent(Event event){
        return eventRepository.save(event);
    }

    //read all events
    public List<Event> getAllEvents(){
        return eventRepository.findAll();
    }

    //read event by id
    public Event getEventById(Long id){
        return eventRepository
                .findById(id)
                .orElseThrow(() -> new NoItemWithThisId("No event with this ID"));
    }

    //update
    //create update for each field

    //delete
    public void deleteEvent(Long id){
        eventRepository.deleteById(id);
    }

    //find upcoming events
    public List<Event> findUpcomingEvents(){
        OffsetDateTime start = OffsetDateTime.now();
        OffsetDateTime end = start.plusDays(60);
        return eventRepository
                .findEventsByDateTimeBetweenOrderByDateTimeAsc(start, end)
                .orElseThrow(() -> new NoUpcomingEvents("There's no upcoming events yet"));
    }
}

