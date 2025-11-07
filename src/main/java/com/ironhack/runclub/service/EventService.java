package com.ironhack.runclub.service;

import com.ironhack.runclub.enums.CitiesEnum;
import com.ironhack.runclub.exceptions.NoItemWithThisId;
import com.ironhack.runclub.exceptions.NoUpcomingEvents;
import com.ironhack.runclub.model.Club;
import com.ironhack.runclub.model.Event;
import com.ironhack.runclub.model.User;
import com.ironhack.runclub.repository.ClubRepository;
import com.ironhack.runclub.repository.EventRepository;
import com.ironhack.runclub.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.List;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final UserService userService;
    private final ClubRepository clubRepository;

    public EventService(EventRepository eventRepository, UserService userService, ClubRepository clubRepository){
        this.eventRepository = eventRepository;
        this.userService = userService;
        this.clubRepository = clubRepository;
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

    public List<Event> getEventsByClub(String clubName){
        Club club = clubRepository.getClubByClubName(clubName);
        return eventRepository.findEventsByClub(club);
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

    //join / leave event
    public void signUpForEvent(Authentication auth, Long eventId) {
        String username = auth.getName();
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
    public List<Event> filterByCity(CitiesEnum city){
        return eventRepository
                .findEventsByCity(city)
                .orElseThrow(() -> new NoItemWithThisId("No events for the chosen city"));
    }

    public List<Event> filterByPace(CitiesEnum city, double min, double max){
        return eventRepository
                .findEventsByCityAndPaceBetween(city, min, max)
                .orElseThrow(() -> new NoItemWithThisId("No events for the chosen pace"));
    }

    public List<Event> filterByDistance(CitiesEnum city, double min, double max){
        return eventRepository
                .findEventsByCityAndDistanceBetween(city, min, max)
                .orElseThrow(() -> new NoItemWithThisId("No events for the chosen distance"));
    }

    public List<Event> getEventsByEventOwner(Authentication auth){
        String username = auth.getName();
        User eventOwner = userService.getUser(username);

        return eventRepository
                .findEventsByEventOwner(eventOwner)
                .orElseThrow(() -> new NoItemWithThisId("No events for this user"));
    }

    public Event createEventByLoggedInUser(Authentication auth, @RequestBody Event event){
        String username = auth.getName();
        User eventOwner = userService.getUser(username);

        if(clubRepository.findClubsByClubOwner(eventOwner).isEmpty()){
            throw new IllegalStateException("Event can't be cerated without a club, user needs to create a club first");
        }

        event.setEventOwner(eventOwner);

        return eventRepository.save(event);
    }

    public void deleteEventByLoggedInUser(Authentication auth, Long id){
        String username = auth.getName();
        User eventOwner = userService.getUser(username);

        Event eventToDelete = eventRepository
                .findEventById(id)
                .orElseThrow(() -> new NoItemWithThisId("No event with this ID"));

        if(!eventToDelete.getEventOwner().equals(eventOwner)){
            throw new IllegalStateException("You're not the event owner");
        }

        eventRepository.delete(eventToDelete);
    }
}

