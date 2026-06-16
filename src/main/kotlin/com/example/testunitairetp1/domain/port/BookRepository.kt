package com.example.testunitairetp1.domain.port

import com.example.testunitairetp1.domain.model.Book

interface BookRepository {
    fun save(book: Book)
    fun findAll(): List<Book>
}
