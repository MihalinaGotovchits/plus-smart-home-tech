package ru.yandex.practicum.kafkaClient;

import lombok.Getter;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Getter
@Configuration
@EnableConfigurationProperties({KafkaConfigProperties.class})
public class KafkaConfig {

    private final KafkaConfigProperties kafkaConfigProperties;

    public KafkaConfig(KafkaConfigProperties properties) {
        this.kafkaConfigProperties = properties;
    }

    @Bean
    public Producer<String, SpecificRecordBase> producer() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfigProperties.getBootstrapServers());
        properties.put(ProducerConfig.CLIENT_ID_CONFIG, kafkaConfigProperties.getClientIdConfig());
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, kafkaConfigProperties.getProducerKeySerializer());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, kafkaConfigProperties.getProducerValueSerializer());
        return new KafkaProducer<>(properties);
    }
}
