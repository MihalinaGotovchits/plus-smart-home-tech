package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.config.KafkaConfig;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaSnapshotServiceImpl implements SnapshotService {
    private final Producer<String, SpecificRecordBase> producer;
    private final KafkaConfig kafkaConfig;
    private final Map<String, SensorsSnapshotAvro> snapshot = new HashMap<>();

    @Override
    public Optional<SensorsSnapshotAvro> updateState(SensorEventAvro event) {
        SensorsSnapshotAvro snapshotAvro = snapshot.getOrDefault(
                event.getHubId(),
                getNewSensorSnapshotAvro(event.getHubId())
        );

        SensorStateAvro oldState = snapshotAvro.getSensorsState().get(event.getId());
        if (oldState != null && oldState.getTimestamp().isAfter(event.getTimestamp()) &&
                oldState.getData().equals(event.getPayload())) {
            return Optional.empty();
        }
        SensorStateAvro newState = getNewSensorSnapshotAvro(event);
        snapshotAvro.getSensorsState().put(event.getId(), newState);
        snapshotAvro.setTimestamp(event.getTimestamp());
        snapshot.put(event.getId(), snapshotAvro);
        return Optional.of(snapshotAvro);
    }

    @Override
    public void collectSensorSnapshot(SensorsSnapshotAvro sensorsSnapshotAvro) {
        ProducerRecord<String, SpecificRecordBase> rec = new ProducerRecord<>(
                kafkaConfig.getKafkaConfigProperties().getSensorSnapshotsTopic(),
                null,
                sensorsSnapshotAvro.getTimestamp().toEpochMilli(),
                sensorsSnapshotAvro.getHubId(),
                sensorsSnapshotAvro
        );
        producer.send(rec);
    }

    @Override
    public void close() {
        SnapshotService.super.close();
        if (producer != null) {
            producer.close();
        }
    }

    private SensorsSnapshotAvro getNewSensorSnapshotAvro(String key) {
        return SensorsSnapshotAvro.newBuilder()
                .setHubId(key)
                .setTimestamp(Instant.now())
                .setSensorsState(new HashMap<>())
                .build();
    }

    private SensorStateAvro getNewSensorSnapshotAvro(SensorEventAvro event) {
        return SensorStateAvro.newBuilder()
                .setTimestamp(event.getTimestamp())
                .setData(event.getPayload())
                .build();
    }
}
