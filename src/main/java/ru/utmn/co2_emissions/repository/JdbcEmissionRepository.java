package ru.utmn.co2_emissions.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.utmn.co2_emissions.model.Emission;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("jdbc")
@RequiredArgsConstructor
public class JdbcEmissionRepository implements EmissionRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Emission> findAll() {
        return jdbcTemplate.query(
                "SELECT id, country, region, year_value, kilotons, metric_tons_per_capita FROM emissions",
                (rs, rowNum) -> {
                    Emission e = new Emission();
                    e.setId(rs.getLong("id"));
                    e.setCountry(rs.getString("country"));
                    e.setRegion(rs.getString("region"));
                    e.setYear(rs.getInt("year_value"));
                    e.setKilotons(rs.getDouble("kilotons"));
                    e.setMetricTonsPerCapita(rs.getDouble("metric_tons_per_capita"));
                    return e;
                }
        );
    }

    @Override
    public Optional<Emission> findById(Long id) {
        List<Emission> list = jdbcTemplate.query(
                "SELECT id, country, region, year_value, kilotons, metric_tons_per_capita FROM emissions WHERE id = ?",
                (rs, rowNum) -> {
                    Emission e = new Emission();
                    e.setId(rs.getLong("id"));
                    e.setCountry(rs.getString("country"));
                    e.setRegion(rs.getString("region"));
                    e.setYear(rs.getInt("year_value"));
                    e.setKilotons(rs.getDouble("kilotons"));
                    e.setMetricTonsPerCapita(rs.getDouble("metric_tons_per_capita"));
                    return e;
                },
                id
        );
        return list.stream().findFirst();
    }

    @Override
    public Emission save(Emission emission) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO emissions (country, region, year_value, kilotons, metric_tons_per_capita) VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, emission.getCountry());
            ps.setString(2, emission.getRegion());
            ps.setInt(3, emission.getYear());
            if (emission.getKilotons() == null) ps.setNull(4, java.sql.Types.DOUBLE); else ps.setDouble(4, emission.getKilotons());
            if (emission.getMetricTonsPerCapita() == null) ps.setNull(5, java.sql.Types.DOUBLE); else ps.setDouble(5, emission.getMetricTonsPerCapita());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key != null) emission.setId(key.longValue());
        return emission;
    }

    @Override
    public Emission update(Long id, Emission emission) {
        int updated = jdbcTemplate.update(
                "UPDATE emissions SET country=?, region=?, year_value=?, kilotons=?, metric_tons_per_capita=? WHERE id=?",
                emission.getCountry(),
                emission.getRegion(),
                emission.getYear(),
                emission.getKilotons(),
                emission.getMetricTonsPerCapita(),
                id
        );
        if (updated == 0) throw new RuntimeException("Not found");
        emission.setId(id);
        return emission;
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM emissions WHERE id = ?", id);
    }
}