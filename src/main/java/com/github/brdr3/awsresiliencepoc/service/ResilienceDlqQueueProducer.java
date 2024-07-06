package com.github.brdr3.awsresiliencepoc.service;

import com.github.brdr3.awsresiliencepoc.configuration.sqs.SqsProperties;
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResilienceDlqQueueProducer  {

    private final SqsProperties properties;
    private final QueueMessagingTemplate queueMessagingTemplate;

    @Retry(name = "retry-dlq-producer")
    public void produceMessage(final String messageText) {
        final Message<String> message = MessageBuilder.withPayload(messageText).build();
        queueMessagingTemplate.send(properties.getResilienceDlq().getQueueName(), message);
    }
}
