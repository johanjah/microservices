package com.library.core.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.core.dto.BookCommand;
import com.library.core.dto.BookCommandReply;
import com.library.core.kafka.producer.BookReplyProducer;
import com.library.core.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookCommandConsumer {

    private final ObjectMapper objectMapper;
    private final BookService bookService;
    private final BookReplyProducer replyProducer;

    @KafkaListener(topics = "library.book.command")
    public void consume(String message) {

        BookCommand command = null;
        try {
            command = objectMapper.readValue(
                    message,
                    BookCommand.class
            );

            Long bookId = bookService.createBook(command);

            // success replay
            replyProducer.send(new BookCommandReply(
                    command.correlationId(),
                    true,
                    bookId,
                    "Book created successfully"
            ));
            System.out.println("Book saved. ID: " + bookId);

        } catch (Exception ex) {
            if (command != null) {
                // produce replay
                replyProducer.send(new BookCommandReply(
                        command.correlationId(),
                        false,
                        null,
                        ex.getMessage() != null
                                ? ex.getMessage()
                                : "Failed to create book"
                ));
            }
            System.err.println(
                    "failed processing command book: " + ex.getMessage()
            );
        }
    }
}
