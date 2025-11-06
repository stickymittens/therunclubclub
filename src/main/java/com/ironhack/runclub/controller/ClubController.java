package com.ironhack.runclub.controller;

import com.ironhack.runclub.exceptions.NoItemWithThisId;
import com.ironhack.runclub.model.Club;
import com.ironhack.runclub.model.User;
import com.ironhack.runclub.service.ClubService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ClubController {
    private final ClubService clubService;

    public ClubController(ClubService clubService){
        this.clubService = clubService;
    }


    //CRUD
    //create club
    @PostMapping("/clubs")
    public Club createClub(Authentication auth, @RequestBody Club club){
        return clubService.createClub(auth, club);
    }

    //read all clubs
    @GetMapping("/clubs")
    public List<Club> getAllClubs(){
        return clubService.getAllClubs();
    }

    //read club by id
    @GetMapping("/clubs/{id}")
    public Club getClubById(@PathVariable Long id){
        return clubService.getClubById(id);
    }

    @GetMapping("/clubs/owned-clubs")
    public List<Club> getClubsForLoggedInUser(Authentication auth){
        return clubService.getClubsForLoggedInUser(auth);
    }


    //update
    //create update for each field

    //delete
    @DeleteMapping("/clubs")
    public void deleteClub(Long id){
        clubService.deleteClub(id);
    }

    @DeleteMapping("/clubs/delete-club/{id}")
    public void deleteClubByClubOwner(Authentication auth, @PathVariable Long id){
        clubService.deleteClubByClubOwner(auth, id);
    }
}
