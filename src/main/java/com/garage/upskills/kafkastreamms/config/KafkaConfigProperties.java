package com.garage.upskills.kafkastreamms.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("spring.kafka")
public class KafkaConfigProperties {

    private String bootStrapServers;
    private String keySerializer;
    private String valueSerializer;

    private String applicationId;

    private String messageTopic;
    private String employeeTopic;
    private String trainingTopic;
}
