package com.example.testunitairetp1

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class CesarCipherTest : FunSpec({

    // Cas nominaux
    test("cipher 'A' with key 2 should return 'C'") {
        // Arrange
        val char = 'A'
        val key = 2

        // Act
        val result = cypher(char, key)

        // Assert
        result shouldBe 'C'
    }

    test("cipher 'A' with key 5 should return 'F'") {
        // Arrange
        val char = 'A'
        val key = 5

        // Act
        val result = cypher(char, key)

        // Assert
        result shouldBe 'F'
    }

    test("cipher 'B' with key 3 should return 'E'") {
        // Arrange
        val char = 'B'
        val key = 3

        // Act
        val result = cypher(char, key)

        // Assert
        result shouldBe 'E'
    }

    // Cas limites
    test("cipher 'Z' with key 1 should wrap around to 'A'") {
        // Arrange
        val char = 'Z'
        val key = 1

        // Act
        val result = cypher(char, key)

        // Assert
        result shouldBe 'A'
    }

    test("cipher 'A' with key 26 should return 'A' (full cycle)") {
        // Arrange
        val char = 'A'
        val key = 26

        // Act
        val result = cypher(char, key)

        // Assert
        result shouldBe 'A'
    }

    test("cipher 'A' with key 27 should return 'B' (key > 26 restarts cycle)") {
        // Arrange
        val char = 'A'
        val key = 27

        // Act
        val result = cypher(char, key)

        // Assert
        result shouldBe 'B'
    }

    test("cipher 'A' with key 0 should return 'A'") {
        // Arrange
        val char = 'A'
        val key = 0

        // Act
        val result = cypher(char, key)

        // Assert
        result shouldBe 'A'
    }

    // Cas pathologiques
    test("cipher with negative key should throw exception") {
        shouldThrow<IllegalArgumentException> {
            cypher('A', -1)
        }
    }

    test("cipher with lowercase letter should throw exception") {
        shouldThrow<IllegalArgumentException> {
            cypher('a', 2)
        }
    }
})
