package com.github.brdr3.awsresiliencepoc.processor;

import com.github.brdr3.awsresiliencepoc.domain.DomainMessage;
import com.github.brdr3.awsresiliencepoc.service.KafkaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Processor {
    private final KafkaService kafkaService;

    public void process(final DomainMessage message) {
        kafkaService.sendMessage(message);
    }
}
