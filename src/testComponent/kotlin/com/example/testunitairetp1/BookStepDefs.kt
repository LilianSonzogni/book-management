package com.example.testunitairetp1

import io.cucumber.java.Before
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.kotest.matchers.shouldBe
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import io.restassured.path.json.JsonPath
import io.restassured.response.Response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

class BookStepDefs {

    @LocalServerPort
    private var port: Int = 0

    @Autowired
    private lateinit var namedParameterJdbcTemplate: NamedParameterJdbcTemplate

    private lateinit var booksResponse: Response
    private var lastReservationStatus: Int = 0

    @Before
    fun setup() {
        RestAssured.baseURI = "http://localhost:$port"
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
        // Isolation : on repart d'une base vide à chaque scénario.
        namedParameterJdbcTemplate.update("DELETE FROM book", EmptySqlParameterSource.INSTANCE)
    }

    @Given("the user adds the book {string} by {string}")
    fun addBook(title: String, author: String) {
        given()
            .contentType(ContentType.JSON)
            .body("""{"title":"$title","author":"$author"}""")
            .`when`()
            .post("/books")
            .then()
            .statusCode(HttpStatus.CREATED.value())
    }

    @When("the user reserves the book {string}")
    fun reserveBook(title: String) {
        given()
            .`when`()
            .post("/books/{title}/reservation", title)
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value())
    }

    @When("the user tries to reserve the book {string} again")
    fun tryToReserveBookAgain(title: String) {
        lastReservationStatus = given()
            .`when`()
            .post("/books/{title}/reservation", title)
            .then()
            .extract()
            .statusCode()
    }

    @When("the user retrieves all the books")
    fun getAllBooks() {
        booksResponse = given()
            .`when`()
            .get("/books")
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .response()
    }

    @Then("the returned list should contain the following books")
    fun shouldReturnBooks(expectedBooks: List<Map<String, String>>) {
        val expectedJson = expectedBooks.joinToString(separator = ",", prefix = "[", postfix = "]") { row ->
            """{"title":"${row["title"]}","author":"${row["author"]}","reserved":${row["reserved"]}}"""
        }
        booksResponse.body().jsonPath().prettify() shouldBe JsonPath(expectedJson).prettify()
    }

    @Then("the reservation is rejected because the book is already reserved")
    fun reservationRejected() {
        lastReservationStatus shouldBe HttpStatus.CONFLICT.value()
    }
}
