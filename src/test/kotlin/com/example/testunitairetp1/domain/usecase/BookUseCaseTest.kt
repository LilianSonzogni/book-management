package com.example.testunitairetp1.domain.usecase

import com.example.testunitairetp1.domain.model.Book
import com.example.testunitairetp1.domain.port.BookRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify

class BookUseCaseTest : FunSpec({

    val bookRepository = mockk<BookRepository>()
    val bookUseCase = BookUseCase(bookRepository)

    // Cas nominaux
    test("addBook should save the book in the repository") {
        // Arrange
        val book = Book("Clean Code", "Robert C. Martin")
        every { bookRepository.save(book) } just runs

        // Act
        bookUseCase.addBook(book)

        // Assert
        verify(exactly = 1) { bookRepository.save(book) }
    }

    test("listBooks should return books sorted alphabetically by title") {
        // Arrange
        val books = listOf(
            Book("The Pragmatic Programmer", "David Thomas"),
            Book("Clean Code", "Robert C. Martin"),
            Book("Design Patterns", "Gang of Four")
        )
        every { bookRepository.findAll() } returns books

        // Act
        val result = bookUseCase.listBooks()

        // Assert
        result shouldBe listOf(
            Book("Clean Code", "Robert C. Martin"),
            Book("Design Patterns", "Gang of Four"),
            Book("The Pragmatic Programmer", "David Thomas")
        )
    }

    // Cas limites
    test("listBooks with empty repository should return empty list") {
        // Arrange
        every { bookRepository.findAll() } returns emptyList()

        // Act
        val result = bookUseCase.listBooks()

        // Assert
        result shouldBe emptyList()
    }

    test("listBooks with single book should return it as is") {
        // Arrange
        val book = Book("Clean Code", "Robert C. Martin")
        every { bookRepository.findAll() } returns listOf(book)

        // Act
        val result = bookUseCase.listBooks()

        // Assert
        result shouldBe listOf(book)
    }

    // Cas pathologiques
    test("addBook with blank title should throw exception") {
        shouldThrow<IllegalArgumentException> {
            Book("", "Robert C. Martin")
        }
    }

    test("addBook with blank author should throw exception") {
        shouldThrow<IllegalArgumentException> {
            Book("Clean Code", "")
        }
    }
})
