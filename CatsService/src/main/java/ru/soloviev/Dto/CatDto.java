package ru.soloviev.Dto;

import lombok.Data;
import ru.soloviev.Models.*;


import java.time.LocalDate;
import java.util.List;

public @Data class CatDto {
    private Integer id;

    private Name name;

    private Breed breed;

    private Color color;

    private LocalDate dateOfBirth;

    private Integer ownerId;

    private List<CatIdDto> friends;
}
