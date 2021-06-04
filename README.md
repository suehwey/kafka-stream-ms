**Kafka MicroService**

1. RESTful proxy: receives message(s) and publish to Kafka topic
   - message structure:
     key: employee, training, benefit, ...
     data: JSON structure of corresponding entity

2. Kafka Stream: listen to topic and convert message to domain object, then publish to Kafka topics based on key

3. bring up zookeeper and kafka server and modify application.properties accordingly before running application