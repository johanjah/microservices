package com.library.core.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.core.dto.BookCommandReply;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class BookReplyProducer {

    private static final String TOPIC = "library.book.reply";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public BookReplyProducer(
            KafkaTemplate<String, String> kafkaTemplate,
            ObjectMapper objectMapper
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void send(BookCommandReply reply) {
        try {
            String message = objectMapper.writeValueAsString(reply);

            kafkaTemplate.send(
                    TOPIC,
                    reply.correlationId(),
                    message
            );
        } catch (JsonProcessingException ex) {
            throw new IllegalArgumentException(
                    "Failed to change BookCommandReply into JSON",
                    ex
            );
        }
    }
}