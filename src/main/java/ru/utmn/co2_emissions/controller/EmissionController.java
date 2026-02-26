package ru.utmn.co2_emissions.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.utmn.co2_emissions.model.Emission;
import ru.utmn.co2_emissions.service.EmissionService;

import java.util.List;

@RestController
@RequestMapping("/api/emissions")
@RequiredArgsConstructor
public class EmissionController {

    private final EmissionService service;

    @GetMapping
    public List<Emission> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Emission getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public Emission create(@RequestBody Emission emission) {
        return service.create(emission);
    }

    @PutMapping("/{id}")
    public Emission update(@PathVariable Long id,
                           @RequestBody Emission emission) {
        return service.update(id, emission);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}