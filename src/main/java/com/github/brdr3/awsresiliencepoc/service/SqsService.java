package com.github.brdr3.awsresiliencepoc.service;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@RequiredArgsConstructor
public class SqsService {

    private final QueueMessagingTemplate queueMessagingTemplate;

    @Value("${sqs.queue-name}")
    private final String queueName;

//    @PostConstruct
//    public void init() {
//        System.out.println("Enviando mensagem");
//
//        Message<String> message = MessageBuilder.withPayload("oi").build();
//
//        queueMessagingTemplate.convertAndSend(queueName, message);
//    }
}
