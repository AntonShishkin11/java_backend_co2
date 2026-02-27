package ru.utmn.co2_emissions.service;

import ru.utmn.co2_emissions.model.Emission;

import java.util.List;

public interface EmissionServiceInterface {
    List<Emission> getAll();
    Emission getById(Long id);
    Emission create(Emission emission);
    Emission update(Long id, Emission emission);
    void delete(Long id);
}