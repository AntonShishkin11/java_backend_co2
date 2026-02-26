package ru.utmn.co2_emissions.repository;

import com.opencsv.CSVReader;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import ru.utmn.co2_emissions.model.Emission;
import ru.utmn.co2_emissions.repository.EmissionRepository;

import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@Profile({"csv","jdbc"})
public class CsvEmissionRepository implements EmissionRepository {

    private final Map<Long, Emission> storage = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @PostConstruct
    public void load() {
        try (CSVReader reader = new CSVReader(
                new InputStreamReader(
                        getClass().getResourceAsStream("/data/co2.csv")))) {

            List<String[]> rows = reader.readAll();
            rows.remove(0);

            for (String[] row : rows) {
                Emission e = new Emission();
                e.setId(idGenerator.getAndIncrement());
                e.setCountry(row[0]);
                e.setRegion(row[1]);
                e.setYear(Integer.parseInt(row[2].substring(6)));
                e.setKilotons(Double.parseDouble(row[3]));
                e.setMetricTonsPerCapita(Double.parseDouble(row[4]));

                storage.put(e.getId(), e);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
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
        emission.setId(idGenerator.getAndIncrement());
        storage.put(emission.getId(), emission);
        return emission;
    }

    @Override
    public Emission update(Long id, Emission emission) {
        emission.setId(id);
        storage.put(id, emission);
        return emission;
    }

    @Override
    public void delete(Long id) {
        storage.remove(id);
    }
}