package ru.yandex.practicum.model.hubEvent;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
public class DeviceAddedEvent extends HubEvent {

    @NotBlank
    private String id;

    @NotNull
    private DeviceType deviceType;

    @Override
    public HubEventType getType() {
        return HubEventType.DEVICE_ADDED;
    }
}
