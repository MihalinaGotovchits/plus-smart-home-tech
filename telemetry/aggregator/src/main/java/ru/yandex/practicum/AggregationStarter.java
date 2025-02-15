package ru.yandex.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.config.KafkaConfig;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.service.SnapshotService;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AggregationStarter {
    private final SnapshotService snapshotService;
    private final Consumer<String, SensorEventAvro> consumer;
    private final KafkaConfig kafkaConfig;
    private final Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();

    public void start() {
        Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));

        try {
            consumer.subscribe(List.of(kafkaConfig.getKafkaConfigProperties().getSensorEventTopic()));
            while (true) {
                ConsumerRecords<String, SensorEventAvro> records = consumer
                        .poll(Duration.ofMillis(Long.parseLong(kafkaConfig.getKafkaConfigProperties()
                                .getConsumerAttemptTimeout())));
                int count = 0;
                for (ConsumerRecord<String, SensorEventAvro> record : records) {

                    handleRecord(record);
                    manageOffsets(record, count, consumer);
                    count++;
                }
                consumer.commitAsync();
            }

        } catch (WakeupException ignores) {

        } catch (Exception e) {
            log.error("Ошибка при обработке событий от датчиков", e);
        } finally {

            try {
                consumer.commitSync(currentOffsets);

            } finally {
                log.info("close consumer");
                consumer.close();
                log.info("close producer");
                snapshotService.close();
            }
        }
    }

    private void handleRecord(ConsumerRecord<String, SensorEventAvro> consumerRecord) throws InterruptedException {
        Optional<SensorsSnapshotAvro> snapshotAvro = snapshotService.updateState(consumerRecord.value());
        snapshotAvro.ifPresent(snapshotService::collectSensorSnapshot);
    }

    private void manageOffsets(ConsumerRecord<String, SensorEventAvro> consumerRecord, int count, Consumer<String, SensorEventAvro> consumer) {
        currentOffsets.put(
                new TopicPartition(consumerRecord.topic(), consumerRecord.partition()),
                new OffsetAndMetadata(consumerRecord.offset() + 1)
        );

        if (count % 10 == 0) {
            consumer.commitAsync(currentOffsets, (offsets, exception) -> {
                if (exception != null) {
                    log.warn("Ошибка при фиксации оффсетов: {}", offsets, exception);
                }
            });
        }
    }
}