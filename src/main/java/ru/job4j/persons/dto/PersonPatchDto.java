package ru.job4j.persons.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PersonPatchDto {
    @NotNull(message = "Password must be not null")
    @Size(min = 8, max = 64, message = "Password must be more than 8 and less 64")
    private String password;
}
