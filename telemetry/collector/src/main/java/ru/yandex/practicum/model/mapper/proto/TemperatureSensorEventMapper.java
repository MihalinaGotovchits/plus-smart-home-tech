package ru.yandex.practicum.model.mapper.proto;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.TemperatureSensorProto;
import ru.yandex.practicum.model.sensorEvent.SensorEvent;
import ru.yandex.practicum.model.sensorEvent.TemperatureSensorEvent;

import java.time.Instant;

@Component
public class TemperatureSensorEventMapper implements SensorEventProtoMapper {
    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.TEMPERATURE_SENSOR_EVENT;
    }

    @Override
    public SensorEvent map(SensorEventProto event) {
        TemperatureSensorProto sensorEvent = event.getTemperatureSensorEvent();

        TemperatureSensorEvent temperatureSensorEvent = TemperatureSensorEvent.builder()
                .id(event.getId())
                .hubId(event.getHubId())
                .timestamp(Instant.ofEpochSecond(event.getTimestamp().getSeconds(), event.getTimestamp().getNanos()))
                .temperatureC(sensorEvent.getTemperatureC())
                .temperatureF(sensorEvent.getTemperatureF())
                .build();
        return temperatureSensorEvent;
    }
}
