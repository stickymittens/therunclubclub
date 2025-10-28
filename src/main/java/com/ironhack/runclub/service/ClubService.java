package com.ironhack.runclub.service;

import com.ironhack.runclub.exceptions.NoItemWithThisId;
import com.ironhack.runclub.model.Club;
import com.ironhack.runclub.repository.ClubRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClubService {
    private final ClubRepository clubRepository;

    public ClubService(ClubRepository clubRepository){
        this.clubRepository = clubRepository;
    }

    //CRUD
    //create
    public Club createClub(Club club){
        return clubRepository.save(club);
    }

    //read all clubs
    public List<Club> getAllClubs(){
        return clubRepository.findAll();
    }

    //read club by id
    public Club getClubById(Long id){
        return clubRepository
                .findById(id)
                .orElseThrow(() -> new NoItemWithThisId("No clubs with this ID"));
    }

    //update
    //create update for each field

    //delete
    public void deleteClub(Long id){
        clubRepository.deleteById(id);
    }
}