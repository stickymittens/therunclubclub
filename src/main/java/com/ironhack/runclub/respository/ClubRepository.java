package com.ironhack.runclub.respository;

import com.ironhack.runclub.model.Club;
import com.ironhack.runclub.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {
    Optional<Club> findClubById(Long id);
}
