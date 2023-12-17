package ru.job4j.persons.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.persons.model.Person;
import ru.job4j.persons.repository.PersonRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;

    public List<Person> findAll() {
        return personRepository.findAll();
    }

    public Optional<Person> save(Person person) {
        try {
            personRepository.save(person);
            return Optional.of(person);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    public Optional<Person> findById(Integer id) {
        return personRepository.findById(id);
    }

    @Transactional
    public Optional<Person> update(Person person) {
        return personRepository.findById(person.getId())
                .map(entity -> personRepository.save(person))
                .stream().findFirst();
    }

    @Transactional
    public boolean delete(Integer id) {
        return personRepository.findById(id)
                .map(entity -> {
                    personRepository.delete(entity);
                    return true;
                })
                .orElse(false);
    }
}