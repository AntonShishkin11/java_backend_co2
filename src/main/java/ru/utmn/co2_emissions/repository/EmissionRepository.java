package ru.utmn.co2_emissions.repository;

import ru.utmn.co2_emissions.model.Emission;

import java.util.List;
import java.util.Optional;

public interface EmissionRepository {
    List<Emission> findAll();
    Optional<Emission> findById(Long id);
    Emission save(Emission emission);
    Emission update(Long id, Emission emission);
    void delete(Long id);
}