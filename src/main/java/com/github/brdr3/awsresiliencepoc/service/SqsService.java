package com.github.brdr3.awsresiliencepoc.service;

import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SqsService {

    private final QueueMessagingTemplate queueMessagingTemplate;

    @Value("${sqs.queue-name}")
    private final String queueName;

}
