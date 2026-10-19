package com.library.orchestrator.controller;

import com.library.orchestrator.dto.BookCommand;
import com.library.orchestrator.dto.BookCommandReply;
import com.library.orchestrator.kafka.producer.KafkaMessageProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/api/kafka")
@RequiredArgsConstructor
public class KafkaTestController {

    private final KafkaMessageProducer producer;

    @PostMapping("/test")
    public ResponseEntity<BookCommandReply> sendBookCommand(
            @RequestParam String title,
            @RequestParam String author,
            @RequestParam String isbn
    ) {
        String correlationId = UUID.randomUUID().toString();

        BookCommand command = new BookCommand(
                correlationId,
                "CREATE_BOOK",
                title,
                author,
                isbn
        );

        try {
            BookCommandReply reply = producer
                    .send(command)
                    .orTimeout(10, TimeUnit.SECONDS)
                    .get();

            if (!reply.success()) {
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body(reply);
            }

            return ResponseEntity.ok(reply);

        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();

            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Request interrupted",
                    ex
            );

        } catch (ExecutionException ex) {
            Throwable cause = ex.getCause();

            if (cause instanceof TimeoutException) {
                throw new ResponseStatusException(
                        HttpStatus.GATEWAY_TIMEOUT,
                        "Library-core didn't having a response in 10 seconds",
                        cause
                );
            }

            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "Failed to send command or get a replay",
                    cause
            );
        }
    }
}
