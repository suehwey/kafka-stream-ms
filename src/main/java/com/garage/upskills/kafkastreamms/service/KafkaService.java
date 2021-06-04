package com.garage.upskills.kafkastreamms.service;

import com.garage.upskills.domain.Message;
import com.garage.upskills.kafkastreamms.producer.MessageProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KafkaService {

    private final Logger logger = LoggerFactory.getLogger(KafkaService.class);

    @Autowired
    private MessageProducer messageProducer;

    public void sendMessage(Message message) {
        logger.info(message.toString());
        // send message to message topic (raw topic)
        messageProducer.sendMessage(message);
    }

    public void sendMessages(List<Message> messages) {
        messages.forEach(this::sendMessage);
    }
}
