package com.library.core.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookDocument {

    @Id
    private Long id;
    private String title;
    private String author;
    private String isbn;

}
