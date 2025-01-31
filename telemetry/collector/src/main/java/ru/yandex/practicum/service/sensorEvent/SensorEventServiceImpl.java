package ru.yandex.practicum.service.sensorEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafkaClient.KafkaConfig;
import ru.yandex.practicum.model.mapper.SensorEventMapper;
import ru.yandex.practicum.model.sensorEvent.SensorEvent;

@Service
@RequiredArgsConstructor
@Slf4j
public class SensorEventServiceImpl implements SensorEventService {

    private final Producer<String, SpecificRecordBase> producer;

    private final KafkaConfig kafkaConfig;

    @Override
    public void collectSensorEvent(SensorEvent event) {
        send(kafkaConfig.getKafkaConfigProperties().getSensorEventTopic(),
                event.getHubId(),
                event.getTimestamp().toEpochMilli(),
                SensorEventMapper.toSensorEventAvro(event));
    }

    private void send(String topic, String key, Long timestamp, SpecificRecordBase specificRecordBase) {
        ProducerRecord<String, SpecificRecordBase> record = new ProducerRecord<>(
                topic,
                null,
                timestamp,
                key,
                specificRecordBase
        );
        producer.send(record);
    }
}