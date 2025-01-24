package ru.yandex.practicum.model.sensorEvent;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString(callSuper = true)
public class SwitchSensorEvent extends SensorEvent {

    @NotNull
    private boolean state;

    @Override
    public SensorEventType getType() {
        return SensorEventType.SWITCH_SENSOR_EVENT;
    }
}
