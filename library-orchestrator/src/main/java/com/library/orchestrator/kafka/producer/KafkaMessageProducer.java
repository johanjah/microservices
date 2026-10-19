package com.library.orchestrator.kafka.producer;

import com.library.orchestrator.dto.BookCommand;
import com.library.orchestrator.dto.BookCommandReply;
import com.library.orchestrator.kafka.consumer.BookReplyRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class KafkaMessageProducer {

    public static final String TOPIC = "library.book.command";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final BookReplyRegistry replyRegistry;

    public void sendMessage(String message) {
        kafkaTemplate.send(TOPIC, message);
    }

    public CompletableFuture<BookCommandReply> send(BookCommand bookCommand) {
        CompletableFuture<BookCommandReply> replyFuture = replyRegistry.register(bookCommand.correlationId());

        String message = objectMapper.writeValueAsString(bookCommand);


        kafkaTemplate.send(
                TOPIC,
                bookCommand.correlationId(),
                message
        ).whenComplete((result, error) -> {
            if (error != null) {
                replyFuture.completeExceptionally(error);
            }
        });

        return replyFuture;
    }
}
