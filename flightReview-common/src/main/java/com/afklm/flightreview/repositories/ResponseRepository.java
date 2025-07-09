package com.afklm.flightreview.repositories;

import com.afklm.flightreview.entities.Response;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResponseRepository extends JpaRepository<Response, Long> {
}
