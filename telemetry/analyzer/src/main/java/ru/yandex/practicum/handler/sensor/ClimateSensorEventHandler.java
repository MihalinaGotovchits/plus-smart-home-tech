package ru.yandex.practicum.handler.sensor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.model.ConditionType;

@Slf4j
@Component
public class ClimateSensorEventHandler implements SensorEventHandler {
    @Override
    public String getType() {
        return ClimateSensorAvro.class.getName();
    }

    @Override
    public Integer getSensorValue(ConditionType type, SensorStateAvro sensorStateAvro) {
        ClimateSensorAvro climateSensorAvro = (ClimateSensorAvro) sensorStateAvro.getData();
        return switch (type) {
            case TEMPERATURE -> climateSensorAvro.getTemperatureC();
            case CO2LEVEL -> climateSensorAvro.getCo2Level();
            case HUMIDITY -> climateSensorAvro.getHumidity();
            default -> null;
        };
    }
}