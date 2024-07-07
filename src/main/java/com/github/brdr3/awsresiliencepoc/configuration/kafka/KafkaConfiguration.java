package com.github.brdr3.awsresiliencepoc.configuration.kafka;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.Map;

@Getter
@EnableAsync
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(KafkaProperties.class)
public class KafkaConfiguration {

    private static final short REPLICATION_FACTOR = 1;

    @Bean
    public KafkaAdmin kafkaAdmin(final KafkaProperties kafkaProperties) {
        return new KafkaAdmin(Map.of(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers()));
    }

    @Bean
    public NewTopic topic(final KafkaProperties kafkaProperties) {
        return new NewTopic(kafkaProperties.getTopicName(), 1, REPLICATION_FACTOR);
    }

    @Bean("producerFactory")
    public ProducerFactory<String, String> producerFactory(final KafkaProperties kafkaProperties) {
        final Map<String, Object> properties = Map.ofEntries(
                Map.entry(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers()),
                Map.entry(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class),
                Map.entry(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class));

        return new DefaultKafkaProducerFactory<>(properties);
    }

    @Bean("producerFactoryWithError")
    public ProducerFactory<String, String> producerFactoryWithError(final KafkaProperties kafkaProperties) {
        final Map<String, Object> properties = Map.ofEntries(
                Map.entry(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers()),
                Map.entry(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class),
                Map.entry(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class));

        return new DefaultKafkaProducerFactory<>(properties);
    }

    @Bean("kafkaTemplate")
    public KafkaTemplate<String, String> kafkaTemplate(@Qualifier("producerFactory") final ProducerFactory<String, String> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean("kafkaTemplateWithError")
    public KafkaTemplate<String, String> kafkaTemplateWithError(@Qualifier("producerFactoryWithError") final ProducerFactory<String, String> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
}
