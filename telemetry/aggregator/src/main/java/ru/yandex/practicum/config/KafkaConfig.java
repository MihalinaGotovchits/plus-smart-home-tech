package ru.yandex.practicum.config;

import lombok.Getter;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

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
        properties.put(ProducerConfig.CLIENT_ID_CONFIG, kafkaConfigProperties.getProducerClientIdConfig());
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, kafkaConfigProperties.getProducerKeySerializer());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, kafkaConfigProperties.getProducerValueSerializer());
        return new KafkaProducer<>(properties);
    }

    @Bean
    public KafkaConsumer<String, SensorEventAvro> getKafkaConsumer() {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfigProperties.getBootstrapServers());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaConfigProperties.getConsumerGroupId());
        properties.put(ConsumerConfig.CLIENT_ID_CONFIG, kafkaConfigProperties.getConsumerClientIdConfig());
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, kafkaConfigProperties.getConsumerKeyDeserializer());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, kafkaConfigProperties.getConsumerValueDeserializer());
        return new KafkaConsumer<>(properties);
    }
}
