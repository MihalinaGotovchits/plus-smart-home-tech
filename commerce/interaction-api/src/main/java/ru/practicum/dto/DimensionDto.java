package ru.practicum.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DimensionDto {
    @NotNull
    @Min(value = 1)
    private Double width;

    @NotNull
    @Min(value = 1)
    private Double height;

    @NotNull
    @Min(value = 1)
    private Double depth;
}
