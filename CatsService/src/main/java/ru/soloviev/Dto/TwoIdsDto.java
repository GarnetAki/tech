package ru.soloviev.Dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

public @Data class TwoIdsDto {
    @NotNull(message = "Id can not be null")
    private CatIdDto cat1;

    @NotNull(message = "Id can not be null")
    private CatIdDto cat2;
}
