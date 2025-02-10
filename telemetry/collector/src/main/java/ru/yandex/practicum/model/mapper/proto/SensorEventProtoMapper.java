package ru.yandex.practicum.model.mapper.proto;

import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.model.sensorEvent.SensorEvent;

public interface SensorEventProtoMapper {
    SensorEventProto.PayloadCase getMessageType();

    SensorEvent map(SensorEventProto event);
}
