package com.garage.upskills.kafkastreamms.controller;

import com.garage.upskills.domain.Message;
import com.garage.upskills.kafkastreamms.service.KafkaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class KafkaController {

    @Autowired
    private KafkaService service;

    @PostMapping("/kafka/addMessage")
    public void saveMessage(@RequestBody Message message) {
        service.sendMessage(message);
    }

    @PostMapping("/kafka/addMessages")
    public void saveMessages(@RequestBody List<Message> messages) {
        service.sendMessages(messages);
    }
}
