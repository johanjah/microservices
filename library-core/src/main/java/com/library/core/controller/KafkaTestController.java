package com.library.core.controller;

import com.library.core.kafka.producer.KafkaMessageProducer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/kafka")
//@RequiredArgsConstructor
public class KafkaTestController {

    private final KafkaMessageProducer producer;

    public KafkaTestController(KafkaMessageProducer producer) {
        this.producer = producer;
    }

    @PostMapping("/test")
    public Map<String, String> sendTestMessage(
            @RequestParam String message) {

        producer.sendMessage(message);

        return Map.of(
                "status", "SENT",
                "message", message
        );
    }
}
