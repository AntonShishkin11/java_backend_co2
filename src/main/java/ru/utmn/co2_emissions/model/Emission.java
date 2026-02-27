package ru.utmn.co2_emissions.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "emissions")
@Data
public class Emission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String country;

    private String region;

    @Column(name = "year_value", nullable = false)
    private Integer year;

    private Double kilotons;

    @Column(name = "metric_tons_per_capita")
    private Double metricTonsPerCapita;
}