package ru.practicum.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pageable {
    @Min(0)
    private int page;

    @Min(1)
    private int size;

    private List<String> sort;
}
