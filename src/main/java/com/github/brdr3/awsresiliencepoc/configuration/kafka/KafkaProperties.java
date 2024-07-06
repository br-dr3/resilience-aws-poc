package com.github.brdr3.awsresiliencepoc.configuration.kafka;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "kafka")
public class KafkaProperties {
    private String bootstrapServers;
    private String topicName;
    private SendProperties send = new SendProperties();

    @Getter
    @Setter
    public static class SendProperties {
        private Double errorRate;
    }
}
