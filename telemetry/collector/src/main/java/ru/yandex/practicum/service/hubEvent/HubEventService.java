package ru.yandex.practicum.service.hubEvent;

import ru.yandex.practicum.model.hubEvent.HubEvent;

public interface HubEventService {
    void collectHubEvent(HubEvent event);
}
