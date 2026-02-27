package ru.utmn.co2_emissions.dto;

public record CountryTrendPoint(
        int year,
        double totalKilotons,
        double avgPerCapita
) {}