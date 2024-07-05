package com.github.brdr3.awsresiliencepoc.listener;

import com.github.brdr3.awsresiliencepoc.service.KafkaService;
import io.awspring.cloud.messaging.listener.SqsMessageDeletionPolicy;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResilienceListener {

    private final KafkaService kafkaService;

    @SqsListener(value = "${sqs.queue-name}", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void handle(Message<String> message) {
        System.out.println("Mensagem recebida..");
        kafkaService.sendMessage(message.getPayload());
    }
}
