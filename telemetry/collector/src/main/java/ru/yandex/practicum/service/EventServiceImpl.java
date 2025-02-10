package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafkaClient.KafkaConfig;
import ru.yandex.practicum.model.hubEvent.HubEvent;
import ru.yandex.practicum.model.mapper.HubEventMapper;
import ru.yandex.practicum.model.mapper.SensorEventMapper;
import ru.yandex.practicum.model.sensorEvent.SensorEvent;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {
    private final Producer<String, SpecificRecordBase> producer;
    private final KafkaConfig kafkaConfig;

    @Override
    public void collectSensorEvent(SensorEvent event) {
    send(kafkaConfig.getKafkaConfigProperties().getSensorEventTopic(),
            event.getHubId(),
            event.getTimestamp().toEpochMilli(),
            SensorEventMapper.toSensorEventAvro(event));
    }

    @Override
    public void collectHubEvent(HubEvent hubEvent) {
        send(kafkaConfig.getKafkaConfigProperties().getHubEventTopic(),
                hubEvent.getHubId(),
                hubEvent.getTimestamp().toEpochMilli(),
                HubEventMapper.toHubEventAvro(hubEvent));
    }

    private void send(String topic, String key, Long timestamp, SpecificRecordBase specificRecordBase) {
        ProducerRecord<String, SpecificRecordBase> rec = new ProducerRecord<>(
                topic,
                null,
                timestamp,
                key,
                specificRecordBase);
        producer.send(rec);
    }
}
