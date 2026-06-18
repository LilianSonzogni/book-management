package com.example.testunitairetp1

private const val ALPHABET_SIZE = 26

fun cypher(char: Char, key: Int): Char {
    require(char in 'A'..'Z') {
        "Seules les lettres majuscules sont autorisées"
    }

    require(key >= 0) {
        "La clé doit être positive"
    }

    val normalizedKey = key % ALPHABET_SIZE

    val shifted = char.code - 'A'.code + normalizedKey

    return ('A'.code + (shifted % ALPHABET_SIZE)).toChar()
}
