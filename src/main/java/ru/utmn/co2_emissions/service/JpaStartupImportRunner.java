package ru.utmn.co2_emissions.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("jpa")
@RequiredArgsConstructor
public class JpaStartupImportRunner implements ApplicationRunner {

    private final ImportFacade importFacade;

    @Override
    public void run(ApplicationArguments args) {
        int imported = importFacade.importIfEmpty();
        System.out.println("[jpa] startup import: " + imported);
    }
}