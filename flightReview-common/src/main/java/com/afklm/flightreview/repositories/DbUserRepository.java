package com.afklm.flightreview.repositories;

import com.afklm.flightreview.entities.DbUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DbUserRepository extends JpaRepository<DbUser, Integer> {
    public DbUser findByUsername(String username);
}