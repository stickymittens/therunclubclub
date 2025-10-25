package com.ironhack.runclub.controller;

import com.ironhack.runclub.model.Club;
import com.ironhack.runclub.service.ClubService;
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
    public Club createClub(@RequestBody Club club){
        return clubService.createClub(club);
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

    //update
    //create update for each field

    //delete
    @DeleteMapping("/clubs")
    public void deleteClub(Long id){
        clubService.deleteClub(id);
    }
}
