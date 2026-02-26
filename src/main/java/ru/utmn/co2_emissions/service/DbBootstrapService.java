package ru.utmn.co2_emissions.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ru.utmn.co2_emissions.model.Emission;
import ru.utmn.co2_emissions.repository.CsvEmissionRepository;
import ru.utmn.co2_emissions.repository.JdbcEmissionRepository;

@Service
@Profile("jdbc")
@RequiredArgsConstructor
public class DbBootstrapService {

    private final JdbcEmissionRepository jdbcRepo;
    private final CsvEmissionRepository csvRepo;

    @PostConstruct
    public void init() {
        if (jdbcRepo.count() > 0) {
            return;
        }

        // берём всё из CSV-репозитория и заливаем в БД
        for (Emission e : csvRepo.findAll()) {
            jdbcRepo.save(e);
        }
    }
}