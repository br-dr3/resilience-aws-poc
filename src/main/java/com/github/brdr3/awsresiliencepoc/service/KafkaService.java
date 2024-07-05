package com.github.brdr3.awsresiliencepoc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaService {

    @Value("${kafka.topic-name}")
    private final String topicName;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(final String message) {
        final ProducerRecord<String, String> topicMessage = new ProducerRecord<>(topicName, message);
        kafkaTemplate.send(topicMessage)
                .addCallback(this::handleSuccess, this::handleError);
    }

    private void handleSuccess(SendResult<String, String> result) {
        final String message = Optional.ofNullable(result).map(SendResult::getProducerRecord).map(ProducerRecord::value).orElse("");
        log.info("Message sent successfully", kv("messageSent", message));
    }

    private void handleError(final Throwable throwable) {
        log.info("Error trying to produce message to kafka", kv("error", throwable));
    }
}
