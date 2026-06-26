package com.example.testunitairetp1.domain.usecase

import com.example.testunitairetp1.domain.model.Book
import com.example.testunitairetp1.domain.port.BookRepository

class BookUseCase(private val bookRepository: BookRepository) {

    fun addBook(book: Book) {
        bookRepository.save(book)
    }

    fun listBooks(): List<Book> {
        return bookRepository.findAll().sortedBy { it.title.lowercase() }
    }

    fun reserveBook(title: String) {
        val book = bookRepository.findByTitle(title)
            ?: throw NoSuchElementException("Le livre \"$title\" n'existe pas")
        check(!book.reserved) { "Le livre \"$title\" est déjà réservé" }
        bookRepository.reserve(title)
    }
}
