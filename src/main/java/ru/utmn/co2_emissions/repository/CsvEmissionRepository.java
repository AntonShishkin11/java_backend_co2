package ru.utmn.co2_emissions.repository;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;
import ru.utmn.co2_emissions.model.Emission;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@Profile("csv")
public class CsvEmissionRepository implements EmissionRepository {

    private static final String CSV_PATH = "data/co2.csv";

    private final Map<Long, Emission> storage = new LinkedHashMap<>();
    private final AtomicLong seq = new AtomicLong(0);

    @PostConstruct
    public void init() {
        loadFromCsv();
    }

    private void loadFromCsv() {
        ClassPathResource resource = new ClassPathResource(CSV_PATH);

        try (CSVReader reader = new CSVReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            String[] header = reader.readNext(); // пропускаем заголовок
            if (header == null) return;

            String[] row;
            while ((row = reader.readNext()) != null) {
                // Формат CSV: Country,Region,Date,Kilotons of Co2,Metric Tons Per Capita
                // Пример: Afghanistan,Asia,01-01-2011,8930,0.31
                Emission e = new Emission();
                e.setCountry(safeStr(row, 0));
                e.setRegion(safeStr(row, 1));
                e.setYear(parseYear(safeStr(row, 2)));
                e.setKilotons(parseDoubleOrNull(safeStr(row, 3)));
                e.setMetricTonsPerCapita(parseDoubleOrNull(safeStr(row, 4)));

                save(e);
            }
        } catch (IOException | CsvValidationException ex) {
            throw new IllegalStateException("Не удалось прочитать CSV из classpath:" + CSV_PATH, ex);
        }
    }

    private static String safeStr(String[] row, int idx) {
        if (row == null || idx < 0 || idx >= row.length) return null;
        String v = row[idx];
        return v == null ? null : v.trim();
    }

    private static Integer parseYear(String date) {
        if (date == null || date.isBlank()) return null;
        // ожидаем что дата вида dd-MM-yyyy, но не доверяем людям
        // берём последние 4 цифры
        String digits = date.replaceAll("[^0-9]", "");
        if (digits.length() >= 4) {
            String yearStr = digits.substring(digits.length() - 4);
            try {
                return Integer.parseInt(yearStr);
            } catch (NumberFormatException ignored) {
            }
        }
        // fallback: пробуем split
        String[] parts = date.split("[-./]");
        if (parts.length > 0) {
            String last = parts[parts.length - 1];
            try {
                return Integer.parseInt(last);
            } catch (NumberFormatException ignored) {
            }
        }
        return null;
    }

    private static Double parseDoubleOrNull(String s) {
        if (s == null || s.isBlank()) return null;
        try {
            return Double.parseDouble(s.replace(",", "."));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public List<Emission> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Optional<Emission> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Emission save(Emission emission) {
        long id = seq.incrementAndGet();
        emission.setId(id);
        storage.put(id, emission);
        return emission;
    }

    @Override
    public Emission update(Long id, Emission emission) {
        if (!storage.containsKey(id)) {
            throw new ru.utmn.co2_emissions.exception.NotFoundException("Emission not found: " + id);
        }
        emission.setId(id);
        storage.put(id, emission);
        return emission;
    }

    @Override
    public void delete(Long id) {
        storage.remove(id);
    }
}