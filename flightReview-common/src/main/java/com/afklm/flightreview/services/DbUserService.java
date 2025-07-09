package com.afklm.flightreview.services;

import com.afklm.flightreview.entities.DbUser;
import com.afklm.flightreview.repositories.DbUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DbUserService {

    private DbUserRepository dbUserRepository;

    public DbUserService(DbUserRepository dbUserRepository) {
        this.dbUserRepository = dbUserRepository;
    }


    public DbUser getDbUserByUsername(String username) {
        return dbUserRepository.findByUsername(username);
    }

    public List<String> getAllUsernames() {
        return dbUserRepository.findAll().stream()
                .map(DbUser::getUsername)
                .toList();
    }
}
