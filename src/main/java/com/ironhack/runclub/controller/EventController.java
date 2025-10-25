package com.ironhack.runclub.controller;

import com.ironhack.runclub.exceptions.NoUpcomingEvents;
import com.ironhack.runclub.model.Event;
import com.ironhack.runclub.service.EventService;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@CrossOrigin(origins = {"http://192.168.1.128:5173", "http://localhost:5173", "http://172.20.10.7:5173"})
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService){
        this.eventService = eventService;
    }


    //CRUD
    //create event
    @PostMapping("/events")
    public Event createEvent(@RequestBody Event event){
        return eventService.createEvent(event);
    }

    //read all events
    @GetMapping("/events")
    public List<Event> getAllEvents(){
        return eventService.getAllEvents();
    }

    //read event by id
    @GetMapping("/events/{id}")
    public Event getEventById(@PathVariable Long id){
        return eventService.getEventById(id);
    }

    //update
    //create update for each field

    //delete
    @DeleteMapping("/events")
    public void deleteEvent(Long id){
        eventService.deleteEvent(id);
    }

    //get upcoming events
    @GetMapping("/events/upcoming")
    public List<Event> findUpcomingEvents(){
        return eventService.findUpcomingEvents();
    }
}
