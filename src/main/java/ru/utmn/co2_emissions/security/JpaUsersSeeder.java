package ru.utmn.co2_emissions.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
@Profile("jpa")
@RequiredArgsConstructor
public class JpaUsersSeeder {

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void seedUsers() {
        if (personRepository.count() > 0) return;

        Person admin = new Person();
        admin.setEmail("admin");
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setRole("ADMIN");
        admin.setEnabled(true);

        Person user = new Person();
        user.setEmail("user");
        user.setPassword(passwordEncoder.encode("user"));
        user.setRole("USER");
        user.setEnabled(true);

        personRepository.save(admin);
        personRepository.save(user);
    }
}