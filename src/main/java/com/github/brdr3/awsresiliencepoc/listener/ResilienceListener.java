package com.github.brdr3.awsresiliencepoc.listener;

import com.github.brdr3.awsresiliencepoc.service.KafkaService;
import io.awspring.cloud.messaging.listener.SqsMessageDeletionPolicy;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResilienceListener {

    private final KafkaService kafkaService;

    @SqsListener(value = "${sqs.resilience.queue-name}", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void handle(final Message<String> message) {
        log.info("Message received", kv("message", message));
        kafkaService.sendMessage(message.getPayload());
    }
}
