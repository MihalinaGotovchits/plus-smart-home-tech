package ru.yandex.practicum.model.hubEvent;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScenarioCondition {

    @NotBlank
    private String sensorId;

    private ConditionType type;

    private ConditionOperation operation;

    private int value;
}
