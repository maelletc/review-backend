package com.afklm.flightreview.controllers;

import com.afklm.flightreview.entities.Flight;
import com.afklm.flightreview.dto.FlightDTO;
import com.afklm.flightreview.services.FlightService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value="/flights", produces = MediaType.APPLICATION_JSON_VALUE)
public class FlightController {
    private final FlightService flightService;
    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }


    @GetMapping()
    @ResponseBody
    public List<FlightDTO> getAllAircraftTypes() {
        List<Flight> flightList = flightService.findAll();
        // Mapping manuel Flight -> FlightDTO
        List<FlightDTO> flightDTOList = flightList.stream().map(flight -> {
            FlightDTO dto = new FlightDTO();
            dto.setId(flight.getId());
            dto.setFlightNumber(flight.getFlightNumber());
            dto.setFlightDate(flight.getFlightDate());
            dto.setAirline(flight.getAirline());
            return dto;
        }).collect(Collectors.toList());
        return flightDTOList;
    }


}
