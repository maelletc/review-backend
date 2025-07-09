package com.afklm.flightreview.entities;

import com.afklm.flightreview.enums.ReviewStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "review")
@SequenceGenerator(name = "review_seq", sequenceName = "review_seq", allocationSize = 1)
public class Review {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "review_seq")
    private Long id;

    @Column(name = "reference")
    private String reference;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "comment")
    private String comment;


    @Column(name = "review_date")
    private LocalDate reviewDate;


    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ReviewStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "flight_id")
    private Flight flight;

    @OneToOne(mappedBy = "review", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Response response;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dbuser_id")
    @JsonBackReference("user-review")
    private DbUser dbUser;
}
