package com.github.brdr3.awsresiliencepoc.listener;

import com.github.brdr3.awsresiliencepoc.domain.DomainMessage;
import com.github.brdr3.awsresiliencepoc.processor.Processor;
import com.github.brdr3.awsresiliencepoc.repository.MessageRepository;
import io.awspring.cloud.messaging.listener.SqsMessageDeletionPolicy;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResilienceListener {

    private final Processor processor;
    private final MessageRepository repository;

    @SqsListener(value = "${sqs.resilience.queue-name}", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void handle(final Message<String> message) {
        log.info("Message received", kv("message", message));

        final String messageId = Optional.ofNullable(message.getHeaders().get("MessageId")).map(Object::toString).orElseThrow();
        final DomainMessage domainMessage = DomainMessage.builder().id(UUID.fromString(messageId)).content(message.getPayload()).build();
        repository.saveReceived(domainMessage);
        processor.process(domainMessage);
    }
}
