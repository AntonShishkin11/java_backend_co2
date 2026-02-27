package ru.utmn.co2_emissions.dto;

public record RegionSummaryItem(
        String region,
        double totalKilotons,
        double avgPerCapita,
        long records
) {}