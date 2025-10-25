package com.ironhack.runclub.controller;

import com.ironhack.runclub.model.User;
import com.ironhack.runclub.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }


    //CRUD
    //create
    @PostMapping("/users")
    public User createUser(@RequestBody User user){
        return userService.createUser(user);
    }

    //read all users
    @GetMapping("/users")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    //read user by id
    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable Long id){
        return userService.getUserById(id);
    }

    //update
    //create update for each field

    //delete
    @DeleteMapping("/users")
    public void deleteUser(Long id){
        userService.deleteUser(id);
    }
}
