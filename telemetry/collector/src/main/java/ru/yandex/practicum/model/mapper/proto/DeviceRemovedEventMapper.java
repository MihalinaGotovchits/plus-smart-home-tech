package ru.yandex.practicum.model.mapper.proto;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.DeviceRemovedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.model.hubEvent.DeviceRemovedEvent;
import ru.yandex.practicum.model.hubEvent.HubEvent;

import java.time.Instant;

@Component
public class DeviceRemovedEventMapper implements HubEventProtoMapper {
    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.DEVICE_REMOVED;
    }

    @Override
    public HubEvent map(HubEventProto event) {
        DeviceRemovedEventProto hubEvent = event.getDeviceRemoved();

        DeviceRemovedEvent deviceRemovedEvent = DeviceRemovedEvent.builder()
                .hubId(event.getHubId())
                .timestamp(Instant.ofEpochSecond(event.getTimestamp().getSeconds(), event.getTimestamp().getNanos()))
                .id(hubEvent.getId())
                .build();
        return deviceRemovedEvent;
    }
}
