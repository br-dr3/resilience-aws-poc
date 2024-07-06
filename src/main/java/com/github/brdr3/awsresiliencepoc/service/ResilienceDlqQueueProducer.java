package com.github.brdr3.awsresiliencepoc.service;

import com.github.brdr3.awsresiliencepoc.configuration.sqs.SqsProperties;
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResilienceDlqQueueProducer  {

    private final SqsProperties properties;
    private final QueueMessagingTemplate queueMessagingTemplate;

    public void produceMessage(final String messageText) {
        final Message<String> message = MessageBuilder.withPayload(messageText).build();
        queueMessagingTemplate.send(properties.getResilienceDlq().getQueueName(), message);
    }
}
