package com.ironhack.runclub.service;

import com.ironhack.runclub.exceptions.NoItemWithThisId;
import com.ironhack.runclub.model.User;
import com.ironhack.runclub.respository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    //CRUD
    //create
    public User createUser(User user){
        return user;
    }

    //read all users
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    //read user by id
    public User getUserById(Long id){
        return userRepository
                .findById(id)
                .orElseThrow(() -> new NoItemWithThisId("No user with this ID"));
    }

    //update
    //create update for each field

    //delete
    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }
}
