package com.library.orchestrator.kafka.consumer;

import com.library.orchestrator.kafka.producer.KafkaMessageProducer;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaMessageConsumer {

    @KafkaListener(topics = KafkaMessageProducer.TOPIC)
    public void consume(String message) {
        System.out.println("========== KAFKA CONSUMER AKTIF ==========");
        System.out.println("Kafka received: " + message);
    }
}
