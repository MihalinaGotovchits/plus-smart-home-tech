package ru.yandex.practicum.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.config.KafkaConfig;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.service.HubEventService;

import java.time.Duration;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class HubEventProcessor implements Runnable {
    private final Consumer<String, HubEventAvro> consumer;
    private final KafkaConfig kafkaConfig;
    private final HubEventService service;

    @Override
    public void run() {
        Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));
        try {
            consumer.subscribe(List.of(kafkaConfig.getKafkaProperties().getHubEventsTopic()));
            while (true) {
                ConsumerRecords<String, HubEventAvro> records = consumer
                        .poll(Duration.ofMillis(kafkaConfig.getKafkaProperties()
                                .getHubConsumer().getAttemptTimeout()));
                for (ConsumerRecord<String, HubEventAvro> record : records) {
                    handleRecord(record);
                }
            }
        } catch (WakeupException ignores) {
            // Игнорируем исключение, так как оно используется для завершения работы
        } catch (Exception e) {
            log.error("Ошибка во время обработки события хаба ", e);
        } finally {
            log.info("Закрываем консьюмер");
            consumer.close();
        }
    }

    private void handleRecord(ConsumerRecord<String, HubEventAvro> consumerRecord) throws InterruptedException {
        log.info("handleRecord {}", consumerRecord);
        service.process(consumerRecord.value());
    }
}