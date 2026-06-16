package com.example.testunitairetp1.infrastructure.driven.postgres

import com.example.testunitairetp1.domain.model.Book
import com.example.testunitairetp1.domain.port.BookRepository
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class BookDAO(private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) : BookRepository {

    override fun save(book: Book) {
        namedParameterJdbcTemplate.update(
            "INSERT INTO book (title, author) VALUES (:title, :author)",
            MapSqlParameterSource()
                .addValue("title", book.title)
                .addValue("author", book.author),
        )
    }

    override fun findAll(): List<Book> =
        namedParameterJdbcTemplate.query("SELECT title, author FROM book", MapSqlParameterSource()) { rs, _ ->
            Book(
                title = rs.getString("title"),
                author = rs.getString("author"),
            )
        }
}
