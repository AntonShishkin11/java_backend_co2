package ru.utmn.co2_emissions.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.utmn.co2_emissions.dto.CountryTrendPoint;
import ru.utmn.co2_emissions.dto.RegionSummaryItem;
import ru.utmn.co2_emissions.dto.TopCountryItem;
import ru.utmn.co2_emissions.model.Emission;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final EmissionServiceInterface emissionService;

    public List<String> listCountries() {
        return emissionService.getAll().stream()
                .map(Emission::getCountry)
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .distinct()
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .toList();
    }

    public List<CountryTrendPoint> countryTrend(String country, Integer fromYear, Integer toYear) {
        String norm = normalize(country);

        return emissionService.getAll().stream()
                .filter(e -> normalize(e.getCountry()).equals(norm))
                .filter(e -> e.getYear() != null)
                .filter(e -> fromYear == null || e.getYear() >= fromYear)
                .filter(e -> toYear == null || e.getYear() <= toYear)
                .collect(Collectors.groupingBy(Emission::getYear))
                .entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> {
                    int year = entry.getKey();
                    List<Emission> list = entry.getValue();

                    double totalKilotons = list.stream()
                            .map(Emission::getKilotons)
                            .filter(Objects::nonNull)
                            .mapToDouble(Double::doubleValue)
                            .sum();

                    Double avgPerCapita = average(list, Emission::getMetricTonsPerCapita);

                    return new CountryTrendPoint(year, totalKilotons, avgPerCapita == null ? 0.0 : avgPerCapita);
                })
                .toList();
    }

    public List<TopCountryItem> topCountries(int year, String metric, int limit) {
        String m = metric == null ? "kilotons" : metric.trim().toLowerCase(Locale.ROOT);
        int lim = Math.max(1, Math.min(limit, 100)); // не надо топ-100000

        List<Emission> yearData = emissionService.getAll().stream()
                .filter(e -> e.getYear() != null && e.getYear() == year)
                .toList();

        Map<String, List<Emission>> byCountry = yearData.stream()
                .filter(e -> e.getCountry() != null && !e.getCountry().isBlank())
                .collect(Collectors.groupingBy(e -> e.getCountry().trim()));

        List<TopCountryItem> items = byCountry.entrySet().stream()
                .map(entry -> {
                    String country = entry.getKey();
                    List<Emission> list = entry.getValue();

                    double value;
                    if ("percapita".equals(m) || "per_capita".equals(m) || "metrictonspcapita".equals(m)) {
                        Double avg = average(list, Emission::getMetricTonsPerCapita);
                        value = avg == null ? 0.0 : avg;
                    } else {
                        value = list.stream()
                                .map(Emission::getKilotons)
                                .filter(Objects::nonNull)
                                .mapToDouble(Double::doubleValue)
                                .sum();
                    }

                    return new TopCountryItem(country, value);
                })
                .sorted(Comparator.comparingDouble(TopCountryItem::value).reversed())
                .limit(lim)
                .toList();

        return items;
    }

    public List<RegionSummaryItem> regionSummary(int year) {
        List<Emission> yearData = emissionService.getAll().stream()
                .filter(e -> e.getYear() != null && e.getYear() == year)
                .toList();

        Map<String, List<Emission>> byRegion = yearData.stream()
                .collect(Collectors.groupingBy(e -> {
                    String r = e.getRegion();
                    if (r == null || r.isBlank()) return "UNKNOWN";
                    return r.trim();
                }));

        return byRegion.entrySet().stream()
                .map(entry -> {
                    String region = entry.getKey();
                    List<Emission> list = entry.getValue();

                    double totalKilotons = list.stream()
                            .map(Emission::getKilotons)
                            .filter(Objects::nonNull)
                            .mapToDouble(Double::doubleValue)
                            .sum();

                    Double avgPerCapita = average(list, Emission::getMetricTonsPerCapita);

                    return new RegionSummaryItem(
                            region,
                            totalKilotons,
                            avgPerCapita == null ? 0.0 : avgPerCapita,
                            list.size()
                    );
                })
                .sorted(Comparator.comparingDouble(RegionSummaryItem::totalKilotons).reversed())
                .toList();
    }

    private static String normalize(String s) {
        if (s == null) return "";
        return s.trim().toLowerCase(Locale.ROOT);
    }

    private static Double average(List<Emission> list, Function<Emission, Double> getter) {
        DoubleSummaryStatistics stats = list.stream()
                .map(getter)
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .summaryStatistics();

        if (stats.getCount() == 0) return null;
        return stats.getAverage();
    }
}