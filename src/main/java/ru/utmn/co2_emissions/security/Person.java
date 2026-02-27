package ru.utmn.co2_emissions.security;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "persons")
@Data
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password; // bcrypt

    @Column(nullable = false)
    private String role; // USER/ADMIN

    @Column(nullable = false)
    private boolean enabled = true;
}