package ru.job4j.persons.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "persons")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    @EqualsAndHashCode.Include
    @NotNull(message = "Login must be not null")
    @Size(min = 6, max = 32, message = "Login must be more than 6 and less 32")
    private String login;

    @NotNull(message = "Password must be not null")
    @Size(min = 8, max = 64, message = "Password must be more than 8 and less 64")
    private String password;
}
