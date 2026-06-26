package com.example.testunitairetp1.infrastructure.driven.postgres

import com.example.testunitairetp1.domain.model.Book
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.testcontainers.containers.PostgreSQLContainer

@SpringBootTest
class BookDAOIT : FunSpec() {

    @Autowired
    lateinit var bookDAO: BookDAO

    @Autowired
    lateinit var namedParameterJdbcTemplate: NamedParameterJdbcTemplate

    init {
        extensions(SpringExtension)

        beforeTest {
            namedParameterJdbcTemplate.update("DELETE FROM book", EmptySqlParameterSource.INSTANCE)
        }

        afterSpec {
            container.stop()
        }

        test("save should persist the book so that findAll returns it") {
            bookDAO.save(Book("Clean Code", "Robert C. Martin"))

            bookDAO.findAll() shouldContainExactlyInAnyOrder listOf(Book("Clean Code", "Robert C. Martin"))
        }

        test("findAll should return every book that was saved") {
            bookDAO.save(Book("Clean Code", "Robert C. Martin"))
            bookDAO.save(Book("Design Patterns", "Gang of Four"))

            bookDAO.findAll() shouldContainExactlyInAnyOrder listOf(
                Book("Clean Code", "Robert C. Martin"),
                Book("Design Patterns", "Gang of Four"),
            )
        }

        test("findAll with an empty database should return an empty list") {
            bookDAO.findAll() shouldBe emptyList()
        }

        test("a freshly saved book should not be reserved") {
            bookDAO.save(Book("Clean Code", "Robert C. Martin"))

            bookDAO.findByTitle("Clean Code") shouldBe Book("Clean Code", "Robert C. Martin", reserved = false)
        }

        test("findByTitle should return null when no book matches") {
            bookDAO.findByTitle("Unknown") shouldBe null
        }

        test("reserve should mark the book as reserved") {
            bookDAO.save(Book("Clean Code", "Robert C. Martin"))

            bookDAO.reserve("Clean Code")

            bookDAO.findByTitle("Clean Code") shouldBe Book("Clean Code", "Robert C. Martin", reserved = true)
        }

        test("reserve should only affect the targeted book") {
            bookDAO.save(Book("Clean Code", "Robert C. Martin"))
            bookDAO.save(Book("Design Patterns", "Gang of Four"))

            bookDAO.reserve("Clean Code")

            bookDAO.findAll() shouldContainExactlyInAnyOrder listOf(
                Book("Clean Code", "Robert C. Martin", reserved = true),
                Book("Design Patterns", "Gang of Four", reserved = false),
            )
        }
    }

    companion object {
        private val container = PostgreSQLContainer<Nothing>("postgres:13-alpine")

        init {
            container.start()
            System.setProperty("spring.datasource.url", container.jdbcUrl)
            System.setProperty("spring.datasource.username", container.username)
            System.setProperty("spring.datasource.password", container.password)
        }
    }
}
