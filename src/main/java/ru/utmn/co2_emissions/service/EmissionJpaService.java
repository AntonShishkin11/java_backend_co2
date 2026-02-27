package ru.utmn.co2_emissions.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ru.utmn.co2_emissions.exception.NotFoundException;
import ru.utmn.co2_emissions.model.Emission;
import ru.utmn.co2_emissions.repository.EmissionJpaRepository;

import java.util.List;

@Service
@Profile("jpa")
@RequiredArgsConstructor
public class EmissionJpaService implements EmissionServiceInterface {

    private final EmissionJpaRepository repository;

    @Override
    public List<Emission> getAll() {
        return repository.findAll();
    }

    @Override
    public Emission getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Emission not found: " + id));
    }

    @Override
    public Emission create(Emission emission) {
        emission.setId(null);
        return repository.save(emission);
    }

    @Override
    public Emission update(Long id, Emission emission) {
        Emission existing = getById(id);
        emission.setId(existing.getId());
        return repository.save(emission);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Emission not found: " + id);
        }
        repository.deleteById(id);
    }
}