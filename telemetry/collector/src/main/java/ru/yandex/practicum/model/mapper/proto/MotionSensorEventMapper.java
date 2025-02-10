package ru.yandex.practicum.model.mapper.proto;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.MotionSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.model.sensorEvent.MotionSensorEvent;
import ru.yandex.practicum.model.sensorEvent.SensorEvent;

import java.time.Instant;

@Component
public class MotionSensorEventMapper implements SensorEventProtoMapper {
    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.MOTION_SENSOR_EVENT;
    }

    @Override
    public SensorEvent map(SensorEventProto event) {
        MotionSensorProto sensorEvent = event.getMotionSensorEvent();

        MotionSensorEvent motionSensorEvent = MotionSensorEvent.builder()
                .id(event.getId())
                .hubId(event.getHubId())
                .timestamp(Instant.ofEpochSecond(event.getTimestamp().getSeconds(), event.getTimestamp().getNanos()))
                .linkQuality(sensorEvent.getLinkQuality())
                .motion(sensorEvent.getMotion())
                .voltage(sensorEvent.getVoltage())
                .build();
        return motionSensorEvent;
    }
}
