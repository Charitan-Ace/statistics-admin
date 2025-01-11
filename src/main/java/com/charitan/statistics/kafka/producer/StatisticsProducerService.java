package com.charitan.statistics.kafka.producer;

import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;

@AllArgsConstructor
public class StatisticsProducerService {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    private void send(String topic, Object payload) {
        kafkaTemplate.send(topic, payload);
    }
}
