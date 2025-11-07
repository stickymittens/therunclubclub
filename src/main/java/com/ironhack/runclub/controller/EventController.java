package com.ironhack.runclub.controller;

import com.ironhack.runclub.enums.CitiesEnum;
import com.ironhack.runclub.model.Event;
import com.ironhack.runclub.model.User;
import com.ironhack.runclub.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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

    @DeleteMapping("/events/owned-events/{id}")
    public void deleteEventForLoggedInUser(Authentication auth, @PathVariable Long id){
        eventService.deleteEventByLoggedInUser(auth, id);
    }

    //get upcoming events
    @GetMapping("/events/upcoming")
    public List<Event> findUpcomingEvents(){
        return eventService.findUpcomingEvents();
    }

    //get signed-up users
    @GetMapping("/events/{id}/signed-up")
    public List<User> getSignedUpUsers(@PathVariable Long id){
        return eventService.getSignedUpUsers(id);
    }


    //sign up for an event
    @PostMapping("events/{id}/sign-up")
    public ResponseEntity<String> signUpForEvent(
            @PathVariable Long id,
            Authentication auth
    ) {
        eventService.signUpForEvent(auth, id);
        return ResponseEntity.ok("Successfully signed up for the event!");
    }

    @DeleteMapping("events/{id}/leave")
    public void leaveTheEvent(
            @PathVariable Long id,
            Authentication auth
    ) {
        eventService.leaveTheEvent(auth, id);
    }


    //FILTERS
    @GetMapping("/events/upcoming/{city}")
    public List<Event> filteredByCity(@PathVariable CitiesEnum city){
        return eventService.filterByCity(city);
    }

    @GetMapping("/events/upcoming/{city}/filtered-by-pace")
    public List<Event> filteredByPace(@PathVariable CitiesEnum city, @RequestParam double min, @RequestParam double max){
        return eventService.filterByPace(city, min, max);
    }

    @GetMapping("/events/upcoming/{city}/filtered-by-distance")
    public List<Event> filteredByDistance(@PathVariable CitiesEnum city, @RequestParam double min, @RequestParam double max){
        return eventService.filterByDistance(city, min, max);
    }

    //owned events
    @GetMapping("/events/owned-events")
    public List<Event> getEventsByEventOwner(Authentication auth){
        return eventService.getEventsByEventOwner(auth);
    }

    @PostMapping("/events/owned-events")
    public Event createEventByLoggedInUser(Authentication auth, @RequestBody Event event){
        return eventService.createEventByLoggedInUser(auth, event);
    }
}
