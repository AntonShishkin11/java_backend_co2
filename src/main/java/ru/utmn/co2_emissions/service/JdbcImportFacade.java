package ru.utmn.co2_emissions.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ru.utmn.co2_emissions.model.Emission;
import ru.utmn.co2_emissions.repository.EmissionRepository;

import java.util.List;

@Service
@Profile("jdbc")
@RequiredArgsConstructor
public class JdbcImportFacade implements ImportFacade {

    private final CsvReaderService csvReaderService;
    private final EmissionRepository emissionRepository;

    @Override
    public int importIfEmpty() {
        // В in-memory H2 это будет "пусто" на каждом запуске. Это нормально.
        if (!emissionRepository.findAll().isEmpty()) return 0;

        List<Emission> emissions = csvReaderService.readAll();
        for (Emission e : emissions) {
            e.setId(null);          // id генерит БД
            emissionRepository.save(e);
        }
        return emissions.size();
    }
}