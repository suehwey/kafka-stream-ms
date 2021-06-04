package com.garage.upskills.kafkastreamms.config;

import com.garage.upskills.domain.Message;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Autowired
    KafkaConfigProperties properties;

    @Bean
    public ProducerFactory<String, Message> producerFactory(){
        Map<String,Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getBootStrapServers());
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, properties.getKeySerializer());
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, properties.getValueSerializer());
        return new DefaultKafkaProducerFactory(config);
    }


    @Bean
    public KafkaTemplate<String, Message> kafkaTemplate(){
        return new KafkaTemplate<String, Message>(producerFactory());
    }

    @Bean
    public NewTopic newMessageTopic() {
        System.out.println();
        return TopicBuilder.name(properties.getMessageTopic()).partitions(1).replicas(1).build();
    }

    @Bean
    public NewTopic newEmployeeTopic() {
        System.out.println();
        return TopicBuilder.name(properties.getEmployeeTopic()).partitions(1).replicas(1).build();
    }

    @Bean
    public NewTopic newTrainingTopic() {
        return TopicBuilder.name(properties.getTrainingTopic()).partitions(1).replicas(1).build();
    }

}
