package com.example.testunitairetp1.domain.usecase

import com.example.testunitairetp1.domain.model.Book
import com.example.testunitairetp1.domain.port.BookRepository
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldBeSortedWith
import io.kotest.property.Arb
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.pair
import io.kotest.property.arbitrary.string
import io.kotest.property.arbitrary.filter
import io.kotest.property.checkAll

class BookUseCasePropertyTest : FunSpec({

    test("listBooks should contain all books that were added") {
        val nonBlankString = Arb.string(1, 20).filter { it.isNotBlank() }

        checkAll(Arb.list(Arb.pair(nonBlankString, nonBlankString), 0..10)) { pairs ->
            val fakeRepo = FakeBookRepository()
            val useCase = BookUseCase(fakeRepo)
            val books = pairs.map { (title, author) -> Book(title, author) }
            books.forEach { useCase.addBook(it) }

            val result = useCase.listBooks()

            books.forEach { result shouldContain it }
        }
    }

    test("listBooks should always return books sorted by title") {
        val nonBlankString = Arb.string(1, 20).filter { it.isNotBlank() }

        checkAll(Arb.list(Arb.pair(nonBlankString, nonBlankString), 0..10)) { pairs ->
            val fakeRepo = FakeBookRepository()
            val useCase = BookUseCase(fakeRepo)
            pairs.map { (title, author) -> Book(title, author) }.forEach { useCase.addBook(it) }

            val result = useCase.listBooks()

            result shouldBeSortedWith compareBy { it.title.lowercase() }
        }
    }
})

private class FakeBookRepository : BookRepository {
    private val books = mutableListOf<Book>()

    override fun save(book: Book) {
        books.add(book)
    }

    override fun findAll(): List<Book> = books.toList()
}
