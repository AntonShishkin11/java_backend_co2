package ru.utmn.co2_emissions.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.utmn.co2_emissions.model.Emission;
import ru.utmn.co2_emissions.repository.EmissionRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmissionService {

    private final EmissionRepository repository;

    public List<Emission> getAll() {
        return repository.findAll();
    }

    public Emission getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));
    }

    public Emission create(Emission emission) {
        return repository.save(emission);
    }

    public Emission update(Long id, Emission emission) {
        return repository.update(id, emission);
    }

    public void delete(Long id) {
        repository.delete(id);
    }
}