package com.ironhack.runclub.model;

import com.ironhack.runclub.enums.CitiesEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull (message = "Name must be set")
    private String userName;

    @NotNull
    @Email
    private String email;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CitiesEnum city;


    //getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return userName;
    }

    public void setName(String name) {
        this.userName = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public CitiesEnum getCity() {
        return city;
    }

    public void setCity(CitiesEnum city) {
        this.city = city;
    }


    //constructors
    public User(String userName, String email, CitiesEnum city) {
        this.userName = userName;
        this.email = email;
        this.city = city;
    }

    public User(){}
}
