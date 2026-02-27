package ru.utmn.co2_emissions.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ru.utmn.co2_emissions.model.Emission;
import ru.utmn.co2_emissions.repository.EmissionJpaRepository;

import java.util.List;

@Service
@Profile("jpa")
@RequiredArgsConstructor
public class JpaImportFacade implements ImportFacade {

    private final CsvReaderService csvReaderService;
    private final EmissionJpaRepository emissionJpaRepository;

    @Override
    public int importIfEmpty() {
        if (emissionJpaRepository.count() > 0) return 0;

        List<Emission> emissions = csvReaderService.readAll();
        emissions.forEach(e -> e.setId(null)); // id генерит БД
        emissionJpaRepository.saveAll(emissions);
        return emissions.size();
    }
}