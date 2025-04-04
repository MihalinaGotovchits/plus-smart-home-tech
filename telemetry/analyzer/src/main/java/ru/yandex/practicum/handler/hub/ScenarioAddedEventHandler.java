package ru.yandex.practicum.handler.hub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;
import ru.yandex.practicum.mapper.Mapper;
import ru.yandex.practicum.model.Action;
import ru.yandex.practicum.model.Condition;
import ru.yandex.practicum.model.Scenario;
import ru.yandex.practicum.repository.ActionRepository;
import ru.yandex.practicum.repository.ConditionRepository;
import ru.yandex.practicum.repository.ScenarioRepository;
import ru.yandex.practicum.repository.SensorRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScenarioAddedEventHandler implements HubEventHandler {
    private final ScenarioRepository scenarioRepository;
    private final SensorRepository sensorRepository;
    private final ActionRepository actionRepository;
    private final ConditionRepository conditionRepository;

    @Override
    public String getType() {
        return ScenarioAddedEventAvro.class.getName();
    }

    @Transactional
    @Override
    public void handle(HubEventAvro hubEventAvro) {
        ScenarioAddedEventAvro scenarioAddedEventAvro = (ScenarioAddedEventAvro) hubEventAvro.getPayload();
        if (!checkSensorsExistenceForHub(getConditionsSensorIds(scenarioAddedEventAvro.getConditions()), hubEventAvro.getHubId())) {
            throw new NotFoundException("Не найдены сенсоры условий сценария");
        }
        if (!checkSensorsExistenceForHub(getActionsSensorIds(scenarioAddedEventAvro.getActions()), hubEventAvro.getHubId())) {
            throw new NotFoundException("Не найдены сенсоры действий сценария");
        }
        Optional<Scenario> scenarioOpt = scenarioRepository.findByHubIdAndName(
                hubEventAvro.getHubId(),
                scenarioAddedEventAvro.getName());
        Scenario scenario;
        String logAction;
        List<Long> oldConditionIds = null;
        List<Long> oldActionIds = null;
        if (scenarioOpt.isEmpty()) {
            scenario = Mapper.mapToScenario(hubEventAvro, scenarioAddedEventAvro);
            logAction = "added";
        } else {
            scenario = scenarioOpt.get();
            oldConditionIds = scenario.getConditions().stream().map(Condition::getId).toList();
            oldActionIds = scenario.getActions().stream().map(Action::getId).toList();

            List<Condition> conditions = new ArrayList<>(scenarioAddedEventAvro.getConditions().stream()
                    .map(conditionAvro -> Mapper.mapToCondition(scenario, conditionAvro))
                    .toList());
            List<Action> actions = new ArrayList<>(scenarioAddedEventAvro.getActions().stream()
                    .map(actionAvro -> Mapper.mapToAction(scenario, actionAvro))
                    .toList());
            scenario.setConditions(conditions);
            scenario.setActions(actions);
            logAction = "updated";
        }
        scenarioRepository.save(scenario);
        log.info("{} scenario {}", logAction, scenario);
        deleteUnusedConditions(oldConditionIds);
        deleteUnusedActions(oldActionIds);
    }

    private List<String> getConditionsSensorIds(Collection<ScenarioConditionAvro> conditionsAvro) {
        return conditionsAvro.stream().map(ScenarioConditionAvro::getSensorId).toList();
    }

    private List<String> getActionsSensorIds(Collection<DeviceActionAvro> actionsAvro) {
        return actionsAvro.stream().map(DeviceActionAvro::getSensorId).toList();
    }

    private boolean checkSensorsExistenceForHub(Collection<String> ids, String hubId) {
        return sensorRepository.existsByIdInAndHubId(ids, hubId);
    }

    private void deleteUnusedConditions(Collection<Long> ids) {
        if (ids != null && !ids.isEmpty())
            conditionRepository.deleteAllById(ids);
    }

    private void deleteUnusedActions(Collection<Long> ids) {
        if (ids != null && !ids.isEmpty())
            actionRepository.deleteAllById(ids);
    }
}
