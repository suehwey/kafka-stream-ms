package com.garage.upskills.kafkastreamms;

import com.garage.upskills.domain.Employee;
import com.garage.upskills.domain.Message;
import com.garage.upskills.domain.Training;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;

@SpringBootApplication
public class KafkaStreamMsApplication {

	private final Logger logger = LoggerFactory.getLogger(KafkaStreamMsApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(KafkaStreamMsApplication.class, args);
	}

}
