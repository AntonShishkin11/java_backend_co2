package ru.utmn.co2_emissions.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import ru.utmn.co2_emissions.model.Emission;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvReaderService {

    private static final String CSV_PATH = "data/co2.csv";

    public List<Emission> readAll() {
        List<Emission> res = new ArrayList<>();
        ClassPathResource resource = new ClassPathResource(CSV_PATH);

        try (CSVReader reader = new CSVReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            String[] header = reader.readNext(); // пропускаем заголовок
            if (header == null) return res;

            String[] row;
            while ((row = reader.readNext()) != null) {
                Emission e = new Emission();
                e.setCountry(safe(row, 0));
                e.setRegion(safe(row, 1));
                e.setYear(parseYear(safe(row, 2)));
                e.setKilotons(parseDoubleOrNull(safe(row, 3)));
                e.setMetricTonsPerCapita(parseDoubleOrNull(safe(row, 4)));
                res.add(e);
            }
        } catch (IOException | CsvValidationException ex) {
            throw new IllegalStateException("Не удалось прочитать CSV из classpath:" + CSV_PATH, ex);
        }

        return res;
    }

    private static String safe(String[] row, int idx) {
        if (row == null || idx < 0 || idx >= row.length) return null;
        String v = row[idx];
        return v == null ? null : v.trim();
    }

    private static Integer parseYear(String date) {
        if (date == null || date.isBlank()) return null;
        // ожидаем dd-MM-yyyy, но берём последние 4 цифры
        String digits = date.replaceAll("[^0-9]", "");
        if (digits.length() >= 4) {
            String yearStr = digits.substring(digits.length() - 4);
            try { return Integer.parseInt(yearStr); } catch (NumberFormatException ignored) {}
        }
        return null;
    }

    private static Double parseDoubleOrNull(String s) {
        if (s == null || s.isBlank()) return null;
        try { return Double.parseDouble(s.replace(",", ".")); }
        catch (NumberFormatException e) { return null; }
    }
}