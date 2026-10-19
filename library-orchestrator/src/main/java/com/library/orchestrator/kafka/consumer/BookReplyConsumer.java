package com.library.orchestrator.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.orchestrator.dto.BookCommandReply;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookReplyConsumer {

    private final ObjectMapper objectMapper;
    private final BookReplyRegistry replyRegistry;

    @KafkaListener(topics = "library.book.reply")
    public void consume(String message) {
        try {
            BookCommandReply reply = objectMapper.readValue(
                    message,
                    BookCommandReply.class
            );

            replyRegistry.complete(reply);

            System.out.println("=== BOOK REPLY RECEIVED ===");
            System.out.println("Correlation ID: " + reply.correlationId());
            System.out.println("Success: " + reply.success());
            System.out.println("Book ID: " + reply.bookId());
            System.out.println("Message: " + reply.message());

        } catch (Exception ex) {
            System.err.println(
                    "Failed to read BookCommandReply: " + ex.getMessage()
            );
        }
    }
}