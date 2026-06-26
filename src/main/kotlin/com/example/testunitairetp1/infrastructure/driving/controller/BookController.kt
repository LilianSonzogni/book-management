package com.example.testunitairetp1.infrastructure.driving.controller

import com.example.testunitairetp1.domain.model.Book
import com.example.testunitairetp1.domain.usecase.BookUseCase
import com.example.testunitairetp1.infrastructure.driving.controller.dto.BookDTO
import com.example.testunitairetp1.infrastructure.driving.controller.dto.BookResponseDTO
import com.example.testunitairetp1.infrastructure.driving.controller.dto.ErrorResponseDTO
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/books")
class BookController(private val bookUseCase: BookUseCase) {

    @GetMapping
    fun getBooks(): List<BookResponseDTO> =
        bookUseCase.listBooks().map { BookResponseDTO(it.title, it.author, it.reserved) }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createBook(@Valid @RequestBody book: BookDTO) {
        bookUseCase.addBook(Book(book.title, book.author))
    }

    @PostMapping("/{title}/reservation")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun reserveBook(@PathVariable title: String) {
        bookUseCase.reserveBook(title)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleIllegalArgument(exception: IllegalArgumentException): ErrorResponseDTO =
        ErrorResponseDTO(exception.message ?: "Requête invalide")

    @ExceptionHandler(NoSuchElementException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleNotFound(exception: NoSuchElementException): ErrorResponseDTO =
        ErrorResponseDTO(exception.message ?: "Ressource introuvable")

    @ExceptionHandler(IllegalStateException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handleConflict(exception: IllegalStateException): ErrorResponseDTO =
        ErrorResponseDTO(exception.message ?: "Conflit")
}
