package com.library.core.repository;

import com.library.core.dto.BookDashboardProjection;
import com.library.core.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BookRepository extends JpaRepository<BookEntity, Long> {

    Optional<BookEntity> findByIsbn(String isbn);

    boolean existsByIsbn(String isbn);

    @Query(value = """
            WITH book_stats AS (
                SELECT
                    COUNT(*) AS total_books,
                    COUNT(DISTINCT author) AS total_authors,
                    COUNT(*) FILTER (
                        WHERE created_at >= CURRENT_TIMESTAMP - INTERVAL '30 days'
                    ) AS books_added_last_30_days,
                    MAX(created_at) AS latest_book_created_at
                FROM core_books
            )
            SELECT
                total_books AS "totalBooks",
                total_authors AS "totalAuthors",
                books_added_last_30_days AS "booksAddedLast30Days",
                latest_book_created_at AS "latestBookCreatedAt"
            FROM book_stats
            """, nativeQuery = true)
    BookDashboardProjection getBookDashboard();
}