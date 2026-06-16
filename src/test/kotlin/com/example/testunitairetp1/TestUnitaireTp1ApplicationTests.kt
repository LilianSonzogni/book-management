package com.example.testunitairetp1

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class CaesarCipherTest {

    @Test
    fun `A avec decalage 2 donne C`() {
        assertEquals('C', cypher('A', 2))
    }

    @Test
    fun `Z avec decalage 1 donne A`() {
        assertEquals('A', cypher('Z', 1))
    }

    @Test
    fun `cycle si key superieure a 26`() {
        assertEquals('C', cypher('A', 28))
    }

    @Test
    fun `erreur si key negative`() {
        assertThrows(IllegalArgumentException::class.java) {
            cypher('A', -1)
        }
    }

    @Test
    fun `erreur si caractere non majuscule`() {
        assertThrows(IllegalArgumentException::class.java) {
            cypher('a', 2)
        }
    }
}