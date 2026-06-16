package com.example.testunitairetp1

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.char
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll

class CaesarCipherPropertyTest : FunSpec({

    test("identité : cypher avec clé 0 retourne le même caractère") {
        checkAll(Arb.char('A'..'Z')) { c ->
            cypher(c, 0) shouldBe c
        }
    }

    test("cycle de 26 : cypher(c, k) == cypher(c, k + 26)") {
        checkAll(Arb.char('A'..'Z'), Arb.int(0..1000)) { c, k ->
            cypher(c, k) shouldBe cypher(c, k + 26)
        }
    }

    test("réversibilité : déchiffrer après chiffrer retourne le caractère original") {
        checkAll(Arb.char('A'..'Z'), Arb.int(0..100)) { c, k ->
            val decryptKey = (26 - k % 26) % 26
            cypher(cypher(c, k), decryptKey) shouldBe c
        }
    }

    test("composition : deux chiffrements successifs équivalent à un chiffrement avec la somme des clés") {
        checkAll(Arb.char('A'..'Z'), Arb.int(0..100), Arb.int(0..100)) { c, k1, k2 ->
            cypher(cypher(c, k1), k2) shouldBe cypher(c, k1 + k2)
        }
    }
})
