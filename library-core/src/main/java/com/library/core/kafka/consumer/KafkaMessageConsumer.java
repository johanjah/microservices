package com.library.core.kafka.consumer;

import com.library.core.kafka.producer.KafkaMessageProducer;
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
