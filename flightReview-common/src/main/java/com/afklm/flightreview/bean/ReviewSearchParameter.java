package com.afklm.flightreview.bean;

import com.afklm.flightreview.enums.Outcome;
import com.afklm.flightreview.enums.ReviewStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Setter
@Getter
@Builder
public class ReviewSearchParameter {

    private String reference;
    private Integer rating;
    private String comment;
    private String airline;
    private ReviewStatus status;
    private String writerName;
    private String flightNumber;
    private Outcome outcome;
    private Date flightDate;

}
