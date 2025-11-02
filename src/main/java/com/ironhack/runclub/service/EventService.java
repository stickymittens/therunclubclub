package com.ironhack.runclub.service;

import com.ironhack.runclub.exceptions.NoItemWithThisId;
import com.ironhack.runclub.exceptions.NoUpcomingEvents;
import com.ironhack.runclub.model.Event;
import com.ironhack.runclub.model.User;
import com.ironhack.runclub.repository.EventRepository;
import com.ironhack.runclub.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.List;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final UserService userService;

    public EventService(EventRepository eventRepository, UserService userService){
        this.eventRepository = eventRepository;
        this.userService = userService;
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


    //get signed-up users
    public List<User> getSignedUpUsers(Long eventId){
        Event event =  eventRepository
                .findEventById(eventId)
                .orElseThrow(() -> new NoItemWithThisId("No event with this ID"));
        return event.getSignedUpUsers();
    }

    public void signUpForEvent(Authentication auth, Long eventId) {
        String username = auth.getName();  // ✅ the username from the JWT
        User userToSignUp = userService.getUser(username);

        Event event = eventRepository.findEventById(eventId)
                .orElseThrow(() -> new NoItemWithThisId("No event with this ID"));

        if (event.getSignedUpUsers().contains(userToSignUp)) {
            throw new IllegalStateException("User already signed up for this event");
        }

        event.getSignedUpUsers().add(userToSignUp);
        eventRepository.save(event);
    }

    public void leaveTheEvent(Authentication auth, Long eventId) {
        String username = auth.getName();
        User userToRemove = userService.getUser(username);

        Event event = eventRepository.findEventById(eventId)
                .orElseThrow(() -> new NoItemWithThisId("No event with this ID"));

        if (!event.getSignedUpUsers().contains(userToRemove)) {
            throw new IllegalStateException("User already signed up for this event");
        }

        event.getSignedUpUsers().remove(userToRemove);
        eventRepository.save(event);
    }

    //FILTERS
//    public List<Event> filterByStartTime(OffsetTime min, OffsetTime max){
//        return eventRepository
//                .findEventsByDateTimeBetween(min, max)
//                .orElseThrow(() -> new NoItemWithThisId("No events for the chosen pace"));
//    }

    public List<Event> filterByPace(double min, double max){
        return eventRepository
                .findEventsByPaceBetween(min, max)
                .orElseThrow(() -> new NoItemWithThisId("No events for the chosen pace"));
    }

    public List<Event> filterByDistance(double min, double max){
        return eventRepository
                .findEventsByDistanceBetween(min, max)
                .orElseThrow(() -> new NoItemWithThisId("No events for the chosen distance"));
    }
}

