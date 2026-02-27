package ru.utmn.co2_emissions.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.utmn.co2_emissions.dto.CountryTrendPoint;
import ru.utmn.co2_emissions.dto.RegionSummaryItem;
import ru.utmn.co2_emissions.dto.TopCountryItem;
import ru.utmn.co2_emissions.service.AnalyticsService;

import java.util.List;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    // 1) список стран
    @GetMapping("/countries")
    public List<String> countries() {
        return analyticsService.listCountries();
    }

    // 2) тренд по стране
    @GetMapping("/country/{country}/trend")
    public List<CountryTrendPoint> countryTrend(
            @PathVariable String country,
            @RequestParam(required = false) Integer fromYear,
            @RequestParam(required = false) Integer toYear
    ) {
        return analyticsService.countryTrend(country, fromYear, toYear);
    }

    // 3) топ стран за год
    @GetMapping("/top")
    public List<TopCountryItem> top(
            @RequestParam int year,
            @RequestParam(required = false, defaultValue = "kilotons") String metric,
            @RequestParam(required = false, defaultValue = "10") int limit
    ) {
        return analyticsService.topCountries(year, metric, limit);
    }

    // 4) сводка по регионам за год
    @GetMapping("/regions")
    public List<RegionSummaryItem> regions(@RequestParam int year) {
        return analyticsService.regionSummary(year);
    }
}