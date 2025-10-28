package com.ironhack.runclub.repository;

import com.ironhack.runclub.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findEventById(Long id);
    Optional<List<Event>> findEventsByDateTimeAfterOrderByDateTimeAsc(OffsetDateTime dateTimeAfter);
    Optional<List<Event>> findEventsByDateTimeBetweenOrderByDateTimeAsc(OffsetDateTime start, OffsetDateTime end);
}
