package com.github.brdr3.awsresiliencepoc.service;

import com.github.brdr3.awsresiliencepoc.configuration.kafka.KafkaProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaService {

    private final ResilienceDlqQueueProducer queueProducer;
    private final KafkaProperties kafkaProperties;

    @Qualifier("kafkaTemplate")
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Qualifier("kafkaTemplateWithError")
    private final KafkaTemplate<String, String> kafkaTemplateWithError;

    @Async
    public void sendMessage(final String message) {
        final String topicName = kafkaProperties.getTopicName();
        final Double errorRate = kafkaProperties.getSend().getErrorRate();
        final Double random = ThreadLocalRandom.current().nextDouble(0, 1);

        if(random.compareTo(errorRate) > 0) {
            log.info(String.format("Random value: %s", random), kv("errorRate", errorRate), kv("kafkaTemplate", "rightKafkaTemplate"));
            final ProducerRecord<String, String> topicMessage = new ProducerRecord<>(topicName, message);
            kafkaTemplate.send(topicMessage).addCallback(this::handleSuccess, t -> handleError(t, message));
        } else {
            log.info(String.format("Random value: %s", random), kv("errorRate", errorRate), kv("kafkaTemplate", "wrongKafkaTemplate"));
            final ProducerRecord<String, String> topicMessage = new ProducerRecord<>(topicName, message);

            ListenableFuture<SendResult<String, String>> send = kafkaTemplateWithError.send(topicMessage);
            send.addCallback(this::handleSuccess, t -> handleError(t, message));
            send.cancel(true);
        }
    }

    private void handleSuccess(SendResult<String, String> result) {
        final Optional<ProducerRecord<String, String>> producerRecord = Optional.ofNullable(result).map(SendResult::getProducerRecord);
        final String message = producerRecord.map(ProducerRecord::value).orElse("");
        final String topic = producerRecord.map(ProducerRecord::topic).orElse("");
        log.info("Message sent successfully", kv("messageSent", message), kv("topic", topic));
    }

    private void handleError(final Throwable throwable, final String message) {
        log.info("Error trying to produce message to kafka. Sending to sqs dlq queue", kv("error", throwable.getMessage()), kv("message", message));
        queueProducer.produceMessage(message);
    }
}
