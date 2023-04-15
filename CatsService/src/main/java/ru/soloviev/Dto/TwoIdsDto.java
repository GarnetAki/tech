package ru.soloviev.Dto;

import lombok.Data;

public @Data class TwoIdsDto {
    private CatIdDto cat1;

    private CatIdDto cat2;
}
