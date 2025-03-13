package ru.yandex.practicum.service;

import ru.yandex.practicum.model.hubEvent.HubEvent;
import ru.yandex.practicum.model.sensorEvent.SensorEvent;

public interface EventService {
    void collectSensorEvent(SensorEvent sensorEvent);

    void collectHubEvent(HubEvent hubEvent);
}
