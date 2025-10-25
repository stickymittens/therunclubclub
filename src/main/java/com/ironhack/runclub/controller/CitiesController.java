package com.ironhack.runclub.controller;

import com.ironhack.runclub.enums.CitiesEnum;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/cities")
@CrossOrigin(origins = {"http://192.168.1.128:5173", "http://localhost:5173", "http://172.20.10.7:5173"})
public class CitiesController {

    @GetMapping
    public List<CitiesEnum> getAllCities() {
        return Arrays.asList(CitiesEnum.values());
    }
}