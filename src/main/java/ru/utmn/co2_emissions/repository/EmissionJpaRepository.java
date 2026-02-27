package ru.utmn.co2_emissions.repository;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.utmn.co2_emissions.model.Emission;

@Repository
@Profile("jpa")
public interface EmissionJpaRepository extends JpaRepository<Emission, Long> {
}