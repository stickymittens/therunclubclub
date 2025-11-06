package com.ironhack.runclub.service;

import com.ironhack.runclub.exceptions.NoItemWithThisId;
import com.ironhack.runclub.model.Club;
import com.ironhack.runclub.model.User;
import com.ironhack.runclub.repository.ClubRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClubService {
    private final ClubRepository clubRepository;
    private final UserService userService;

    public ClubService(ClubRepository clubRepository, UserService userService){
        this.clubRepository = clubRepository;
        this.userService = userService;
    }

    //CRUD
    //create
    public Club createClub(Authentication auth, Club club){
        String username = auth.getName();
        User clubOwner = userService.getUser(username);

        club.setClubOwner(clubOwner);

        return clubRepository.save(club);
    }

    public List<Club> getAllClubs(){
        return clubRepository.findAll();
    }

    public Club getClubById(Long id){
        return clubRepository
                .findById(id)
                .orElseThrow(() -> new NoItemWithThisId("No clubs with this ID"));
    }

    public List<Club> getClubsForLoggedInUser(Authentication auth){
        String username = auth.getName();
        User clubOwner = userService.getUser(username);

        return clubRepository
                .findClubsByClubOwner(clubOwner)
                .orElseThrow(() -> new NoItemWithThisId("No clubs for this user"));
    }


    //update
    //create update for each field

    //delete
    public void deleteClub(Long id){
        clubRepository.deleteById(id);
    }

    public void deleteClubByClubOwner(Authentication auth, Long id){
        String username = auth.getName();
        User clubOwner = userService.getUser(username);

        Club clubToDelete = clubRepository
                .findClubById(id)
                .orElseThrow(() -> new NoItemWithThisId("No club with this ID"));

        if(!clubToDelete.getClubOwner().equals(clubOwner)){
            throw new IllegalStateException("You're not teh club owner");
        }

        clubRepository.delete(clubToDelete);
    }
}