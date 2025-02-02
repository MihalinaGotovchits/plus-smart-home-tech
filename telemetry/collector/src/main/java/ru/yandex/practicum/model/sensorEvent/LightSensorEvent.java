package ru.yandex.practicum.model.sensorEvent;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString(callSuper = true)
public class LightSensorEvent extends SensorEvent {

    @NotNull
    private int linkQuality;

    @NotNull
    private int luminosity;

    @Override
    public SensorEventType getType() {
        return SensorEventType.LIGHT_SENSOR_EVENT;
    }
}
