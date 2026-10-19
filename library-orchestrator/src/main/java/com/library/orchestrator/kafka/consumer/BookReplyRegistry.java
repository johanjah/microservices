package com.library.orchestrator.kafka.consumer;

import com.library.orchestrator.dto.BookCommandReply;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class BookReplyRegistry {

    private final ConcurrentMap<
            String,
            CompletableFuture<BookCommandReply>
            > pendingReplies = new ConcurrentHashMap<>();

    public CompletableFuture<BookCommandReply> register(
            String correlationId
    ) {
        CompletableFuture<BookCommandReply> future = new CompletableFuture<>();

        CompletableFuture<BookCommandReply> existing = pendingReplies.putIfAbsent(correlationId, future);

        if (existing != null) {
            throw new IllegalStateException(
                    "Correlation ID already registered: " + correlationId
            );
        }

        future.whenComplete((reply, error) ->
                pendingReplies.remove(correlationId, future)
        );

        return future;
    }

    public void complete(BookCommandReply reply) {
        CompletableFuture<BookCommandReply> future =
                pendingReplies.remove(reply.correlationId());

        if (future != null) {
            future.complete(reply);
        } else {
            System.out.println(
                    "no request for correlationId: " + reply.correlationId()
            );
        }
    }
}