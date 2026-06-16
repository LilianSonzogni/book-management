package com.example.testunitairetp1

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TestUnitaireTp1Application

fun main(args: Array<String>) {
    runApplication<TestUnitaireTp1Application>(*args)
}

fun cypher(char: Char, key: Int): Char {

    require(char in 'A'..'Z') {
        "Seules les lettres majuscules sont autorisées"
    }

    require(key >= 0) {
        "La clé doit être positive"
    }

    val normalizedKey = key % 26

    val shifted = char.code - 'A'.code + normalizedKey

    return ('A'.code + (shifted % 26)).toChar()
}