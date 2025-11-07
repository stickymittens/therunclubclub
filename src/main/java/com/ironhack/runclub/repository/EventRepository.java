package com.ironhack.runclub.repository;

import com.ironhack.runclub.enums.CitiesEnum;
import com.ironhack.runclub.model.Club;
import com.ironhack.runclub.model.Event;
import com.ironhack.runclub.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findEventById(Long id);

    Optional<List<Event>> findEventsByDateTimeBetweenOrderByDateTimeAsc(OffsetDateTime start, OffsetDateTime end);

    Optional<List<Event>> findEventsByCity(CitiesEnum city);
    Optional<List<Event>> findEventsByCityAndPaceBetween(CitiesEnum city, double minPace, double maxPace);
    Optional<List<Event>> findEventsByCityAndDistanceBetween(CitiesEnum city, double minDistance, double maxDistance);

    Optional<List<Event>> findEventsByEventOwner(User eventOwner);

    List<Event> findEventsByClub(Club club);
}
