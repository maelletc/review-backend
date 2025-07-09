package com.afklm.flightreview.repositories;

import com.afklm.flightreview.entities.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightRepository extends JpaRepository<Flight, Long> {
}
