package com.garage.upskills.kafkastreamms.stream;

import com.garage.upskills.domain.Employee;
import com.garage.upskills.domain.Message;
import com.garage.upskills.domain.Training;
import com.garage.upskills.kafkastreamms.config.KafkaConfigProperties;
import com.garage.upskills.util.DateConstant;
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.LinkedHashMap;

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

//        sourceStream.print(Printed.<String, Message>toSysOut().withLabel("Original Stream"));
//        sourceStream.map((k,v) -> KeyValue.pair(k, convertToClass(k, v.getData())));
//        sourceStream.print(Printed.<String, Message>toSysOut().withLabel("Converted Stream"));

        sourceStream.filter(isEmployee())
                .map((k,v) -> KeyValue.pair(k, (Employee) convertToClass(k, v.getData())))
                .filter(this::isNotNull)
                .to(properties.getEmployeeTopic(), Produced.with(stringSerde, employeeSerde));

        sourceStream.filter(isTraining())
                .map((k,v) -> KeyValue.pair(k, (Training) convertToClass(k, v.getData())))
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

    private Object convertToClass (String key, Object data) {

        Object returnObject;

        try {
            Class<?> loadClass;

            switch (key) {
                case "employee":
                    loadClass = Class.forName(Employee.class.getName());
                    returnObject = new Employee();
                    break;
                case "training":
                    loadClass = Class.forName(Training.class.getName());
                    returnObject = new Training();
                    break;
                default:
                    logger.warn("invalid key - " + key);
                    return null;
            }

            LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) data;
            for (var field: map.keySet()) {
                String methodName = "set" + Character.toUpperCase(field.charAt(0)) + field.substring(1);
                Method method;
                if (field.contains("Date")) {
                    method = loadClass.getMethod(methodName, LocalDate.class);
                    method.invoke(returnObject, LocalDate.parse(map.get(field), DateConstant.DATE_TIME_FORMATTER));
                }
                else {
                    method = loadClass.getMethod(methodName, String.class);
                    method.invoke(returnObject, map.get(field));
                }

            }

        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }

        return returnObject;
    }

}
