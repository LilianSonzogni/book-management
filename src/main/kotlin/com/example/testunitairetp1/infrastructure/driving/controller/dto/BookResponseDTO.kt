package com.example.testunitairetp1.infrastructure.driving.controller.dto

/** Représentation renvoyée en sortie, incluant la disponibilité du livre. */
data class BookResponseDTO(
    val title: String,
    val author: String,
    val reserved: Boolean,
)
