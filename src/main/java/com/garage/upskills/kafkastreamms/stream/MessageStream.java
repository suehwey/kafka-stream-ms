package com.garage.upskills.kafkastreamms.stream;

import com.garage.upskills.domain.Employee;
import com.garage.upskills.domain.Message;
import com.garage.upskills.domain.Training;
import com.garage.upskills.kafkastreamms.config.KafkaConfigProperties;
import com.garage.upskills.util.DateConstant;
import com.garage.upskills.util.DomainObjectUtil;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonSerde;

@Configuration
public class MessageStream {
    @Autowired
    KafkaConfigProperties properties;

    private final Logger logger = LoggerFactory.getLogger(MessageStream.class);

    @Bean
    public KStream<String, Message> messageKStream(StreamsBuilder builder) {
        var stringSerde = Serdes.String();
        var messageSerde = new JsonSerde<>(Message.class);
        var employeeSerde = new JsonSerde<>(Employee.class);
        var trainingSerde = new JsonSerde<>(Training.class);

        KStream<String, Message> sourceStream = builder
                .stream(properties.getMessageTopic(), Consumed.with(stringSerde, messageSerde));

//        sourceStream.print(Printed.<String, Message>toSysOut().withLabel("*** Original Stream"));
//        sourceStream.map((k,v) -> KeyValue.pair(k, convertToClass(k, v.getData())));
//        sourceStream.print(Printed.<String, Message>toSysOut().withLabel("*** Converted Stream"));

        sourceStream.filter(isEmployee())
                .map((k, v) -> KeyValue.pair(k, (Employee) DomainObjectUtil.convertToClass(k, v.getData())))
                .filter(this::isNotNull)
                .to(properties.getEmployeeTopic(), Produced.with(stringSerde, employeeSerde));

        sourceStream.filter(isTraining())
                .map((k, v) -> KeyValue.pair(k, (Training) DomainObjectUtil.convertToClass(k, v.getData())))
                .filter(this::isNotNull)
                .to(properties.getTrainingTopic(), Produced.with(stringSerde, trainingSerde));

        return sourceStream;
    }

    private boolean isNotNull(String s, Object o) {
        return o != null;
    }


    public static Predicate<? super String, ? super Message> isEmployee() {
        return (key, value) -> key.equalsIgnoreCase("employee");
    }

    public static Predicate<? super String, ? super Message> isTraining() {
        return (key, value) -> key.equalsIgnoreCase("training");
    }
}

