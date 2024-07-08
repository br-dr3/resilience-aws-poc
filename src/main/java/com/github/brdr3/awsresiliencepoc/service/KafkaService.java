package com.github.brdr3.awsresiliencepoc.service;

import com.github.brdr3.awsresiliencepoc.configuration.kafka.KafkaProperties;
import com.github.brdr3.awsresiliencepoc.domain.DomainMessage;
import com.github.brdr3.awsresiliencepoc.repository.MessageRepository;
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

import static com.github.brdr3.awsresiliencepoc.domain.enuns.Status.ERROR;
import static com.github.brdr3.awsresiliencepoc.domain.enuns.Status.SUCCESS;
import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaService {

    private final ReplayDlqQueueProducer queueProducer;
    private final KafkaProperties kafkaProperties;
    private final MessageRepository repository;

    @Qualifier("kafkaTemplate")
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Qualifier("kafkaTemplateWithError")
    private final KafkaTemplate<String, String> kafkaTemplateWithError;

    @Async
    public void sendMessage(final DomainMessage domainMessage) {
        final String message = domainMessage.getContent();
        final String topicName = kafkaProperties.getTopicName();
        final Double errorRate = kafkaProperties.getSend().getErrorRate();
        final Double random = ThreadLocalRandom.current().nextDouble(0, 1);

        if(random.compareTo(errorRate) > 0) {
            log.info(String.format("Random value: %s", random), kv("errorRate", errorRate), kv("kafkaTemplate", "rightKafkaTemplate"));
            final ProducerRecord<String, String> topicMessage = new ProducerRecord<>(topicName, message);
            kafkaTemplate.send(topicMessage).addCallback(r -> handleSuccess(domainMessage, r), t -> handleError(domainMessage, t));
        } else {
            log.info(String.format("Random value: %s", random), kv("errorRate", errorRate), kv("kafkaTemplate", "wrongKafkaTemplate"));
            final ProducerRecord<String, String> topicMessage = new ProducerRecord<>(topicName, message);

            ListenableFuture<SendResult<String, String>> send = kafkaTemplateWithError.send(topicMessage);
            send.addCallback(result -> handleSuccess(domainMessage, result), t -> handleError(domainMessage, t));
            send.cancel(true);
        }
    }

    private void handleSuccess(final DomainMessage domainMessage, final SendResult<String, String> result) {
        final Optional<ProducerRecord<String, String>> producerRecord = Optional.ofNullable(result).map(SendResult::getProducerRecord);
        final String message = producerRecord.map(ProducerRecord::value).orElse("");
        final String topic = producerRecord.map(ProducerRecord::topic).orElse("");
        log.info("Message sent successfully", kv("messageSent", message), kv("topic", topic));
        repository.saveProduced(domainMessage, SUCCESS);
    }

    private void handleError(final DomainMessage message, final Throwable throwable) {
        log.info("Error trying to produce message to kafka. Sending to sqs dlq queue", kv("error", throwable.getMessage()), kv("message", message));
        queueProducer.produceMessage(message.getContent());
        repository.saveProduced(message, ERROR);
    }
}
