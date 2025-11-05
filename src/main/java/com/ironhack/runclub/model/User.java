package com.ironhack.runclub.model;

import com.ironhack.runclub.enums.CitiesEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static jakarta.persistence.FetchType.EAGER;

/**
 * Entity class for representing a User in the database
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String username;

    private String password;

    @ManyToMany(fetch = EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Collection<Role> roles = new ArrayList<>();

    @ManyToMany(mappedBy = "signedUpUsers")
    private List<Event> signedUpEvents;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CitiesEnum preferredCity;


    //getters and setters
    public List<Event> getSignedUpEvents() {
        return signedUpEvents;
    }

    public CitiesEnum getPreferredCity() {
        return preferredCity;
    }

    public void setPreferredCity(CitiesEnum preferredCity) {
        this.preferredCity = preferredCity;
    }


    //constructors
    public User(String name, String username, String password, CitiesEnum preferredCity) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.preferredCity = preferredCity;
    }
}
