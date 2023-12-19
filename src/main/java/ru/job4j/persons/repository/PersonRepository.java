package ru.job4j.persons.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.job4j.persons.model.Person;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends CrudRepository<Person, Integer> {
    List<Person> findAll();

    @Modifying
    @Query("delete from Person p where p.id = ?1")
    int delete(Integer id);

    Optional<Person> findByLogin(String username);
}
