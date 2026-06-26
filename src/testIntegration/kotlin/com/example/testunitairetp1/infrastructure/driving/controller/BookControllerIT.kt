package com.example.testunitairetp1.infrastructure.driving.controller

import com.example.testunitairetp1.domain.model.Book
import com.example.testunitairetp1.domain.usecase.BookUseCase
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.spring.SpringExtension
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(BookController::class)
class BookControllerIT : FunSpec() {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockkBean
    lateinit var bookUseCase: BookUseCase

    init {
        extensions(SpringExtension)

        // Cas nominaux
        test("GET /books should return the books with their reservation status") {
            every { bookUseCase.listBooks() } returns listOf(
                Book("Clean Code", "Robert C. Martin", reserved = true)
            )

            mockMvc.perform(get("/books"))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""[{"title":"Clean Code","author":"Robert C. Martin","reserved":true}]"""))

            verify(exactly = 1) { bookUseCase.listBooks() }
        }

        test("POST /books should create the book in the domain") {
            every { bookUseCase.addBook(any()) } just runs

            mockMvc.perform(
                post("/books")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""{"title":"Clean Code","author":"Robert C. Martin"}""")
            ).andExpect(status().isCreated)

            verify(exactly = 1) { bookUseCase.addBook(Book("Clean Code", "Robert C. Martin")) }
        }

        // Cas d'erreur sur le contrat d'entrée
        test("POST /books with a blank title should return 400 and not call the domain") {
            mockMvc.perform(
                post("/books")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""{"title":"","author":"Robert C. Martin"}""")
            ).andExpect(status().isBadRequest)

            verify(exactly = 0) { bookUseCase.addBook(any()) }
        }

        // Exception simulée du domaine
        test("POST /books should return 400 when the domain rejects the book") {
            every { bookUseCase.addBook(any()) } throws IllegalArgumentException("Le titre ne peut pas être vide")

            mockMvc.perform(
                post("/books")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""{"title":"Clean Code","author":"Robert C. Martin"}""")
            ).andExpect(status().isBadRequest)
        }

        // Réservation - cas nominal
        test("POST /books/{title}/reservation should reserve the book and return 204") {
            every { bookUseCase.reserveBook("Clean Code") } just runs

            mockMvc.perform(post("/books/{title}/reservation", "Clean Code"))
                .andExpect(status().isNoContent)

            verify(exactly = 1) { bookUseCase.reserveBook("Clean Code") }
        }

        // Réservation - exceptions simulées du domaine
        test("POST reservation should return 409 when the book is already reserved") {
            every { bookUseCase.reserveBook("Refactoring") } throws
                IllegalStateException("Le livre \"Refactoring\" est déjà réservé")

            mockMvc.perform(post("/books/{title}/reservation", "Refactoring"))
                .andExpect(status().isConflict)
        }

        test("POST reservation should return 404 when the book does not exist") {
            every { bookUseCase.reserveBook("Unknown") } throws
                NoSuchElementException("Le livre \"Unknown\" n'existe pas")

            mockMvc.perform(post("/books/{title}/reservation", "Unknown"))
                .andExpect(status().isNotFound)
        }
    }
}
