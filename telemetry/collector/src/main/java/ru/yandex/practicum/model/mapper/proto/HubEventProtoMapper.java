package ru.yandex.practicum.model.mapper.proto;

import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.model.hubEvent.HubEvent;

public interface HubEventProtoMapper {
    HubEventProto.PayloadCase getMessageType();

    HubEvent map(HubEventProto event);
}
