package com.afklm.flightreview.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;


@Setter
@Getter
@NoArgsConstructor  // Constructeur par défaut explicite
@Entity
@Table(name = "flight")
@SequenceGenerator(name = "flight_seq", sequenceName = "flight_seq", allocationSize = 1)
public class Flight {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "flight_seq")
    private Long id;

    @Column(name = "flight_number")
    private String flightNumber;

    @Column(name = "flight_date")
    private LocalDate flightDate;

    @Column(name = "airline")
    private String airline;

    @OneToMany(mappedBy = "flight", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore  // Empêche la sérialisation des reviews dans le flight pour éviter la référence circulaire
    private List<Review> reviews;

}