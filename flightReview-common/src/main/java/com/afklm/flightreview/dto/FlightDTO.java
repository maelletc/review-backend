package com.afklm.flightreview.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class FlightDTO {
    private Long id;
    private String flightNumber;
    private LocalDate flightDate;
    private String airline;
}

