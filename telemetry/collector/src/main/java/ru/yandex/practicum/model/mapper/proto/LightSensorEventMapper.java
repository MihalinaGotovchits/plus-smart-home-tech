package ru.yandex.practicum.model.mapper.proto;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.LightSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.model.sensorEvent.LightSensorEvent;
import ru.yandex.practicum.model.sensorEvent.SensorEvent;

import java.time.Instant;

@Component
public class LightSensorEventMapper implements SensorEventProtoMapper {
    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.LIGHT_SENSOR_EVENT;
    }

    @Override
    public SensorEvent map(SensorEventProto event) {
        LightSensorProto sensorEvent = event.getLightSensorEvent();

        LightSensorEvent lightSensorEvent = LightSensorEvent.builder()
                .id(event.getId())
                .hubId(event.getHubId())
                .timestamp(Instant.ofEpochSecond(event.getTimestamp().getSeconds(), event.getTimestamp().getNanos()))
                .luminosity(sensorEvent.getLuminosity())
                .linkQuality(sensorEvent.getLinkQuality())
                .build();
        return lightSensorEvent;
    }
}
