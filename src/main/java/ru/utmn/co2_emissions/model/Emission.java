package ru.utmn.co2_emissions.model;

import lombok.Data;

@Data
public class Emission {

    private Long id;

    private String country;

    private String region;

    private Integer year;

    private Double kilotons;

    private Double metricTonsPerCapita;
}