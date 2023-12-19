package ru.job4j.persons.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.job4j.persons.model.Person;
import ru.job4j.persons.service.PersonService;

import java.util.List;

@RestController
@RequestMapping("/persons")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    private final PasswordEncoder encoder;

    @GetMapping
    public List<Person> findAll() {
        return personService.findAll();
    }

    @PostMapping
    public ResponseEntity<Person> create(@RequestBody Person person) {
        person.setPassword(encoder.encode(person.getPassword()));
        return personService.save(person)
                .map(result -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(result))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(@PathVariable Integer id) {
        return personService.findById(id)
                .map(result -> ResponseEntity.ok().body(result))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping
    public ResponseEntity<Person> update(@RequestBody Person person) {
        return personService.update(person)
                .map(result -> ResponseEntity.ok().body(result))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_MODIFIED).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Person> delete(@PathVariable Integer id) {
        return personService.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
