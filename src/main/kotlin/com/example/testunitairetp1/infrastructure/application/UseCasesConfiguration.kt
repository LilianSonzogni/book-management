package com.example.testunitairetp1.infrastructure.application

import com.example.testunitairetp1.domain.port.BookRepository
import com.example.testunitairetp1.domain.usecase.BookUseCase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UseCasesConfiguration {

    @Bean
    fun bookUseCase(bookRepository: BookRepository): BookUseCase {
        return BookUseCase(bookRepository)
    }
}
