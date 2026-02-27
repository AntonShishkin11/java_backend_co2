package ru.utmn.co2_emissions.security;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

@Profile("jpa")
public interface PersonRepository extends JpaRepository<Person, Long> {
    Person findByEmailIgnoreCase(String email);
}