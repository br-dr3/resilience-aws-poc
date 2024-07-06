package com.github.brdr3.awsresiliencepoc.configuration.sqs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "sqs")
public class SqsProperties {

    private final QueueProperty resilience = new QueueProperty();
    private final QueueProperty resilienceDlq = new QueueProperty();

    @Getter
    @Setter
    public static class QueueProperty {
        private String queueName;
    }
}
