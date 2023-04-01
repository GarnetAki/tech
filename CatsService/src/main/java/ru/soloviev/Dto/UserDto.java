package ru.soloviev.Dto;

import lombok.Data;
import ru.soloviev.Models.Name;

import java.time.LocalDate;

public @Data class UserDto {
    private Integer id;

    private Name name;

    private LocalDate dateOfBirth;
}
