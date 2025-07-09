package com.afklm.flightreview.controllers;

import com.afklm.flightreview.entities.DbUser;
import com.afklm.flightreview.services.DbUserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value="/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

    private final DbUserService dbUserService;
    public AuthController(DbUserService dbUserService) {
        this.dbUserService = dbUserService;
    }


    @GetMapping("/{username}")
    public DbUser getUser(@PathVariable String username) {
        return dbUserService.getDbUserByUsername(username);
    }

    @GetMapping("/usernames")
    public List<String> getAllUsernames() {
        return dbUserService.getAllUsernames();
    }
}
