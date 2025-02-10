package ru.yandex.practicum.model.sensorEvent;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
public class MotionSensorEvent extends SensorEvent {

    @NotNull
    private int linkQuality;

    @NotNull
    private boolean motion;

    @NotNull
    private int voltage;

    @Override
    public SensorEventType getType() {
        return SensorEventType.MOTION_SENSOR_EVENT;
    }
}
