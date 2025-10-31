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
import java.util.List;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public EventService(EventRepository eventRepository, UserRepository userRepository){
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
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


    //sign up for an event
    public void signUpForEvent(Authentication auth, Long eventId){
        User userToSignUp = (User) auth.getPrincipal();

        Event event = eventRepository
                .findEventById(eventId)
                .orElseThrow(() -> new NoItemWithThisId("No event with this ID"));

        event.getSignedUpUsers().add(userToSignUp);
        eventRepository.save(event);
    }

    //remove yourself from event
    public void removeYourselfFromEvent(Long eventId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User userToRemove = (User) auth.getPrincipal();

        Event event = eventRepository
                .findEventById(eventId)
                .orElseThrow(() -> new NoItemWithThisId("No event with this ID"));

        if (!event.getSignedUpUsers().contains(userToRemove)) {
            throw new NoItemWithThisId("User not signed up for this event");
        }

        event.getSignedUpUsers().remove(userToRemove);
        eventRepository.save(event);
    }
}

