package ru.yandex.practicum.model.mapper.proto;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioRemovedEventProto;
import ru.yandex.practicum.model.hubEvent.HubEvent;
import ru.yandex.practicum.model.hubEvent.ScenarioRemovedEvent;

import java.time.Instant;

@Component
public class ScenarioRemovedEventMapper implements HubEventProtoMapper {
    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.SCENARIO_REMOVED;
    }

    @Override
    public HubEvent map(HubEventProto event) {
        ScenarioRemovedEventProto hubEvent = event.getScenarioRemoved();

        ScenarioRemovedEvent scenarioRemovedEvent = ScenarioRemovedEvent.builder()
                .hubId(event.getHubId())
                .timestamp(Instant.ofEpochSecond(event.getTimestamp().getSeconds(), event.getTimestamp().getNanos()))
                .name(hubEvent.getName())
                .build();
        return scenarioRemovedEvent;
    }
}
