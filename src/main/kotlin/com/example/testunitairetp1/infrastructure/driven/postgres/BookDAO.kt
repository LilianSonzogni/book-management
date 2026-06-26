package com.example.testunitairetp1.infrastructure.driven.postgres

import com.example.testunitairetp1.domain.model.Book
import com.example.testunitairetp1.domain.port.BookRepository
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class BookDAO(private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) : BookRepository {

    override fun save(book: Book) {
        namedParameterJdbcTemplate.update(
            "INSERT INTO book (title, author, reserved) VALUES (:title, :author, :reserved)",
            MapSqlParameterSource()
                .addValue("title", book.title)
                .addValue("author", book.author)
                .addValue("reserved", book.reserved),
        )
    }

    override fun findAll(): List<Book> =
        namedParameterJdbcTemplate.query(
            "SELECT title, author, reserved FROM book",
            MapSqlParameterSource(),
            bookRowMapper,
        )

    override fun findByTitle(title: String): Book? =
        namedParameterJdbcTemplate.query(
            "SELECT title, author, reserved FROM book WHERE title = :title",
            MapSqlParameterSource().addValue("title", title),
            bookRowMapper,
        ).firstOrNull()

    override fun reserve(title: String) {
        namedParameterJdbcTemplate.update(
            "UPDATE book SET reserved = true WHERE title = :title",
            MapSqlParameterSource().addValue("title", title),
        )
    }

    private val bookRowMapper = RowMapper { rs, _ ->
        Book(
            title = rs.getString("title"),
            author = rs.getString("author"),
            reserved = rs.getBoolean("reserved"),
        )
    }
}
