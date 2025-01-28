package ru.yandex.practicum.service.hubEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafkaClient.KafkaConfig;
import ru.yandex.practicum.model.hubEvent.HubEvent;
import ru.yandex.practicum.model.mapper.HubEventMapper;

@Service
@RequiredArgsConstructor
@Slf4j
public class HubEventServiceImpl implements HubEventService {

    private final Producer<String, SpecificRecordBase> producer;

    private final KafkaConfig kafkaConfig;

    @Override
    public void collectHubEvent(HubEvent event) {
        send(kafkaConfig.getKafkaConfigProperties().getHubEventTopic(),
                event.getHubId(),
                event.getTimestamp().toEpochMilli(),
                HubEventMapper.toHubEventAvro(event));
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