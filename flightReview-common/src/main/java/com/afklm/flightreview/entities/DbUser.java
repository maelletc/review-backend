package com.afklm.flightreview.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.afklm.flightreview.enums.Role;

import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "dbuser")
@SequenceGenerator(name = "dbuser_seq", sequenceName = "dbuser_seq", allocationSize = 1)
public class DbUser {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dbuser_seq")
    private Integer id;

    @Column(name = "username")
    private String username;


    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @OneToMany(mappedBy = "dbUser")
    @JsonManagedReference("user-review")
    private List<Review> reviews;

    @OneToMany(mappedBy = "dbUser")
    @JsonManagedReference("admin-response")
    private List<Response> responses;

}