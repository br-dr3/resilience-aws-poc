package com.github.brdr3.awsresiliencepoc.service;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KafkaService {

    @Value("${kafka.topic-name}")
    private final String topicName;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(final String message) {
        ProducerRecord<String, String> topicMessage = new ProducerRecord<>(topicName, message);
        kafkaTemplate.send(topicMessage)
                .addCallback(this::handleSuccess, this::handleError);
    }

    private void handleSuccess(SendResult<String, String> stringStringSendResult) {
        System.out.println("Mensagem enviada com sucesso");
    }

    private void handleError(final Throwable throwable) {
        System.out.println("Erro ao produzir mensagem para o kafka");
        System.out.println(throwable.getMessage());
    }
}
