package com.ironhack.runclub.demo;

import com.ironhack.runclub.enums.CitiesEnum;
import com.ironhack.runclub.model.Club;
import com.ironhack.runclub.model.Event;
import com.ironhack.runclub.model.User;
import com.ironhack.runclub.respository.ClubRepository;
import com.ironhack.runclub.respository.EventRepository;
import com.ironhack.runclub.respository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.OffsetDateTime;

@Configuration
public class DataLoader {
    @Bean
    CommandLineRunner loadData(
            UserRepository userRepository,
            ClubRepository clubRepository,
            EventRepository eventRepository
    ){
        return args -> {
            if(eventRepository.count() == 0){


                //users
                User user1 = userRepository.save(new User("Samantha Jones", "sjones@email.com", CitiesEnum.BARCELONA));
                User user2 = userRepository.save(new User("Alex Brown", "abrown@email.com", CitiesEnum.BARCELONA));
                User user3 = userRepository.save(new User("Ala Ho", "aho@email.com", CitiesEnum.BARCELONA));

                //clubs
                Club club1 = clubRepository.save(new Club("Itinerari", null));
                Club club2 = clubRepository.save(new Club("Razze CLub", null));

                //events
                Event event1 = eventRepository.save(new Event(
                        OffsetDateTime.parse("2025-12-06T11:00:00+01:00"),
                        "Nomad Frutas Selectas",
                        7.0,
                        5.75,
                        null,
                        club1
                ));

                Event event2 = eventRepository.save(new Event(
                        OffsetDateTime.parse("2025-12-07T09:30:00+01:00"),
                        "Arc de Triumph",
                        10.0,
                        5.5,
                        "The spiciest run in Barcelona",
                        club2
                ));

                Event event3 = eventRepository.save(new Event(
                        OffsetDateTime.parse("2024-12-07T09:30:00+01:00"),
                        "Old location",
                        10.0,
                        5.0,
                        null,
                        club2
                ));

                Event event5 = eventRepository.save(new Event(
                        OffsetDateTime.parse("2025-11-08T11:00:00+01:00"),
                        "Syra Coffee Poblenou",
                        7.0,
                        5.75,
                        null,
                        club1
                ));

                Event event11 = eventRepository.save(new Event(
                        OffsetDateTime.parse("2025-11-09T09:30:00+01:00"),
                        "Arc de Triumph",
                        12.0,
                        5.5,
                        "The spiciest run in Barcelona",
                        club2
                ));

                Event event12 = eventRepository.save(new Event(
                        OffsetDateTime.parse("2025-11-15T10:00:00+01:00"),
                        "Arc de Triumph",
                        9.0,
                        5.5,
                        "The spiciest run in Barcelona",
                        club2
                ));

                Event event6 = eventRepository.save(new Event(
                        OffsetDateTime.parse("2025-11-15T09:30:00+01:00"),
                        "Pecora",
                        7.0,
                        5.75,
                        null,
                        club1
                ));

                Event event7 = eventRepository.save(new Event(
                        OffsetDateTime.parse("2025-11-22T11:30:00+01:00"),
                        "Amateur",
                        7.0,
                        5.75,
                        null,
                        club1
                ));

                Event event8 = eventRepository.save(new Event(
                        OffsetDateTime.parse("2025-11-29T11:30:00+01:00"),
                        "Nomad Passeige Nomad Passeige Nomad",
                        7.0,
                        5.75,
                        null,
                        club1
                ));

            }
        };
    }
}
