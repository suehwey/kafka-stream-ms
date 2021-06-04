package com.garage.upskills.kafkastreamms.producer;

import com.garage.upskills.domain.Message;
import com.garage.upskills.kafkastreamms.config.KafkaConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageProducer {

    @Autowired
    KafkaConfigProperties topics;

    @Autowired
    private KafkaTemplate<String, Message> kafkaTemplate;

    public void sendMessage(Message message)  {
        kafkaTemplate.send(topics.getMessageTopic(), message.getKey(), message);
    }

}
