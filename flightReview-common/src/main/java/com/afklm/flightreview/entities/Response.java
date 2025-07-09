package com.afklm.flightreview.entities;

import com.afklm.flightreview.enums.Outcome;
import com.afklm.flightreview.enums.ReviewStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Entity
@Table(name = "response")
@SequenceGenerator(name = "response_seq", sequenceName = "response_seq", allocationSize = 1)
public class Response {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "response_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "content")
    private String content;

    @Column(name = "response_date")
    private LocalDate responseDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "outcome")
    private Outcome outcome;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    @JsonBackReference
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dbuser_id")
    @JsonBackReference("admin-response")
    private DbUser dbUser;
}
