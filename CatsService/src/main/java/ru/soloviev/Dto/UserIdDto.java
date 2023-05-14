package ru.soloviev.Dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public @Data class UserIdDto {
    @NotNull(message = "Id can not be null")
    private Integer id;
}
