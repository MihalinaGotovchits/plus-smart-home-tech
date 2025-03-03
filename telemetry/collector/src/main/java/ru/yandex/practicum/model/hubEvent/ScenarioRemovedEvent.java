package ru.yandex.practicum.model.hubEvent;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
public class ScenarioRemovedEvent extends HubEvent {

    @NotBlank
    private String name;

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_REMOVED;
    }
}
