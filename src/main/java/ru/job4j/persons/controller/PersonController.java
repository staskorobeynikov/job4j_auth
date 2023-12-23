package ru.job4j.persons.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.persons.dto.PersonPatchDto;
import ru.job4j.persons.model.Person;
import ru.job4j.persons.service.PersonService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/persons")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    private final ObjectMapper objectMapper;

    @GetMapping
    public ResponseEntity<List<Person>> findAll() {
        return ResponseEntity
                .ok()
                .body(personService.findAll());
    }

    @PostMapping
    public ResponseEntity<Person> create(@RequestBody Person person) {
        if (person.getLogin() == null || person.getPassword() == null) {
            throw new NullPointerException("Login and password mustn't be empty");
        }
        Person result = personService.save(person);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(@PathVariable Integer id) {
        return personService.findById(id)
                .map(result -> ResponseEntity.ok().body(result))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, String.format("Person with id: %s not found.", id)
                ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Person> update(@PathVariable Integer id,
                                         @RequestBody Person person) {
        if (person.getLogin() == null || person.getPassword() == null) {
            throw new NullPointerException("Login and password mustn't be empty");
        }
        return personService.update(id, person)
                .map(result -> ResponseEntity.ok().body(result))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_MODIFIED).build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Person> patch(@PathVariable Integer id,
                                        @RequestBody PersonPatchDto personPatchDto) {
        if (personPatchDto.getPassword() == null) {
            throw new NullPointerException("Password mustn't be empty");
        }
        return personService.patch(id, personPatchDto)
                .map(result -> ResponseEntity.ok().body(result))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Person> delete(@PathVariable Integer id) {
        return personService.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @ExceptionHandler(value = {DataIntegrityViolationException.class})
    public void exceptionHandler(Exception exception, HttpServletRequest request,
                                 HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.CONFLICT.value());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(new HashMap<>() { {
            put("message", "User can't be created, username is exists...");
            put("type", exception.getClass());
        }}));
        log.error(exception.getLocalizedMessage(), exception);
    }
}
