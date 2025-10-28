package com.ironhack.runclub.demo;

import com.ironhack.runclub.enums.CitiesEnum;
import com.ironhack.runclub.model.Club;
import com.ironhack.runclub.model.Event;
import com.ironhack.runclub.model.Role;
import com.ironhack.runclub.model.User;
import com.ironhack.runclub.repository.ClubRepository;
import com.ironhack.runclub.repository.EventRepository;
import com.ironhack.runclub.repository.RoleRepository;
import com.ironhack.runclub.repository.UserRepository;
import com.ironhack.runclub.service.RoleService;
import com.ironhack.runclub.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.OffsetDateTime;

@Configuration
public class DataLoader {
    @Bean
    CommandLineRunner loadData(
            UserService userService,
            RoleService roleService,
            ClubRepository clubRepository,
            EventRepository eventRepository
    ){
        return args -> {
            if(eventRepository.count() == 0){


                //users
                roleService.save(new Role("ROLE_USER"));
                roleService.save(new Role("ROLE_ADMIN"));

                userService.saveUser(new User("John Doe", "john", "1234"));
                userService.saveUser(new User("James Smith", "james", "1234"));
                userService.saveUser(new User("Jane Carry", "jane", "1234"));
                userService.saveUser(new User("Chris Anderson", "chris", "1234"));

                roleService.addRoleToUser("john", "ROLE_USER");
                roleService.addRoleToUser("james", "ROLE_ADMIN");
                roleService.addRoleToUser("jane", "ROLE_USER");
                roleService.addRoleToUser("chris", "ROLE_ADMIN");
                roleService.addRoleToUser("chris", "ROLE_USER");

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
