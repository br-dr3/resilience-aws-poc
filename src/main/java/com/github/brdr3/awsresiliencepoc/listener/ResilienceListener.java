package com.github.brdr3.awsresiliencepoc.listener;

import io.awspring.cloud.messaging.listener.SqsMessageDeletionPolicy;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

@Service
public class ResilienceListener {

    @SqsListener(value = "${sqs.queue-name}", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void handle(Message<String> message) {
        System.out.println(message.getPayload());
    }
}
