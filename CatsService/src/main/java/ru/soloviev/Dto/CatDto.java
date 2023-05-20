package ru.soloviev.Dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.soloviev.Models.Breed;
import ru.soloviev.Models.Color;
import ru.soloviev.Models.Name;

import java.time.LocalDate;
import java.util.List;

public @Data class CatDto {
    private Integer id;

    @NotNull(message = "Name can not be null")
    private Name name;

    @NotNull(message = "Breed can not be null")
    private Breed breed;

    @NotNull(message = "Color can not be null")
    private Color color;

    @NotNull(message = "Date of Birth can not be null")
    private LocalDate dateOfBirth;

    @NotNull(message = "Owner can not be null")
    private Integer ownerId;

    private List<CatIdDto> friends;
}
