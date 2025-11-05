package com.ironhack.runclub.controller;



import com.ironhack.runclub.enums.CitiesEnum;
import com.ironhack.runclub.model.Event;
import com.ironhack.runclub.model.User;
import com.ironhack.runclub.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;

    /**
     * Get a list of all users
     *
     * @return list of all users
     */
    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getUsers() {
        return userService.getUsers();
    }

    /**
     * Save a new user
     *
     * @param user the user to be saved
     */
    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveUser(@RequestBody User user) {
        userService.saveUser(user);
    }

    @GetMapping("/joined-events")
    @ResponseStatus(HttpStatus.OK)
    public List<Event> getMyEvents(Authentication auth) {
        return userService.getEventsForLoggedInUser(auth);
    }

    @GetMapping("/preferred-city")
    @ResponseStatus(HttpStatus.OK)
    public CitiesEnum getPreferredCity(Authentication auth) {
        return userService.getPreferredCity(auth);
    }
}
