package ru.yandex.practicum.handler.sensor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;
import ru.yandex.practicum.model.ConditionType;

@Slf4j
@Component
public class SwitchSensorEventHandler implements SensorEventHandler {
    @Override
    public String getType() {
        return SwitchSensorAvro.class.getName();
    }

    @Override
    public Integer getSensorValue(ConditionType type, SensorStateAvro sensorStateAvro) {
        SwitchSensorAvro switchSensorAvro = (SwitchSensorAvro) sensorStateAvro.getData();
        return switch (type) {
            case SWITCH -> switchSensorAvro.getState() ? 1 : 0;
            default -> null;
        };
    }
}
