package ru.utmn.co2_emissions.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.utmn.co2_emissions.model.Emission;
import ru.utmn.co2_emissions.repository.EmissionRepository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile({"csv","jdbc"})
@Primary
@RequiredArgsConstructor
public class JdbcEmissionRepository implements EmissionRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Emission> mapper = (rs, rowNum) -> {
        Emission e = new Emission();
        e.setId(rs.getLong("id"));
        e.setCountry(rs.getString("country"));
        e.setRegion(rs.getString("region"));
        e.setYear(rs.getInt("year_value"));
        e.setKilotons(rs.getDouble("kilotons"));
        e.setMetricTonsPerCapita(rs.getDouble("metric_tons_per_capita"));
        return e;
    };

    @Override
    public List<Emission> findAll() {
        return jdbcTemplate.query("SELECT * FROM emissions ORDER BY id", mapper);
    }

    @Override
    public Optional<Emission> findById(Long id) {
        List<Emission> list = jdbcTemplate.query(
                "SELECT * FROM emissions WHERE id = ?",
                mapper,
                id
        );
        return list.stream().findFirst();
    }

    @Override
    public Emission save(Emission emission) {
        jdbcTemplate.update(
                "INSERT INTO emissions (id, country, region, year_value, kilotons, metric_tons_per_capita) " +
                        "VALUES (?, ?, ?, ?, ?, ?)",
                emission.getId(),
                emission.getCountry(),
                emission.getRegion(),
                emission.getYear(),
                emission.getKilotons(),
                emission.getMetricTonsPerCapita()
        );
        return emission;
    }

    @Override
    public Emission update(Long id, Emission emission) {
        jdbcTemplate.update(
                "UPDATE emissions SET country=?, region=?, year_value=?, kilotons=?, metric_tons_per_capita=? WHERE id=?",
                emission.getCountry(),
                emission.getRegion(),
                emission.getYear(),
                emission.getKilotons(),
                emission.getMetricTonsPerCapita(),
                id
        );
        emission.setId(id);
        return emission;
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM emissions WHERE id=?", id);
    }

    // доп. метод: сколько записей
    public long count() {
        Long cnt = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM emissions", Long.class);
        return cnt == null ? 0 : cnt;
    }

    public long nextId() {
        Long max = jdbcTemplate.queryForObject("SELECT COALESCE(MAX(id), 0) FROM emissions", Long.class);
        return (max == null ? 0 : max) + 1;
    }
}