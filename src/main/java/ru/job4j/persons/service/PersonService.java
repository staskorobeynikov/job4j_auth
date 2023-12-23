package ru.job4j.persons.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.persons.dto.PersonPatchDto;
import ru.job4j.persons.model.Person;
import ru.job4j.persons.repository.PersonRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class PersonService implements UserDetailsService {

    private final PersonRepository personRepository;

    private final PasswordEncoder encoder;

    public List<Person> findAll() {
        return personRepository.findAll();
    }

    public Person save(Person person) {
        person.setPassword(encoder.encode(person.getPassword()));
        return personRepository.save(person);
    }

    public Optional<Person> findById(Integer id) {
        return personRepository.findById(id);
    }

    @Transactional
    public Optional<Person> update(Integer id, Person person) {
        person.setPassword(encoder.encode(person.getPassword()));
        return personRepository.findById(id)
                .map(entity -> personRepository.save(person))
                .stream()
                .findFirst();
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return personRepository.findByLogin(username)
                .map(person -> new User(person.getLogin(), person.getPassword(), new ArrayList<>()))
                .orElseThrow(() -> {
                    throw new UsernameNotFoundException(username);
                });
    }

    @Transactional
    public Optional<Person> patch(Integer id, PersonPatchDto personPatchDto) {
        return personRepository.findById(id)
                .map(entity -> {
                    entity.setPassword(encoder.encode(personPatchDto.getPassword()));
                    return entity;
                })
                .map(personRepository::save);
    }
}
