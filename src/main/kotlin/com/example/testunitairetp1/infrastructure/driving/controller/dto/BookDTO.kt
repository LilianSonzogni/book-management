package com.example.testunitairetp1.infrastructure.driving.controller.dto

import jakarta.validation.constraints.NotBlank

/** Représentation reçue en entrée lors de la création d'un livre. */
data class BookDTO(
    @field:NotBlank(message = "Le titre ne peut pas être vide")
    val title: String,
    @field:NotBlank(message = "L'auteur ne peut pas être vide")
    val author: String,
)
