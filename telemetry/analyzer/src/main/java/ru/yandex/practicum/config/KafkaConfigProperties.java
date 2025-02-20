package ru.yandex.practicum.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import ru.yandex.practicum.config.ConsumerProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "kafka.config")
public class KafkaConfigProperties {
    private String bootstrapServers;
    private ConsumerProperties hubConsumer;
    private ConsumerProperties snapshotConsumer;

    private String hubEventsTopic;
    private String sensorSnapshotsTopic;
}