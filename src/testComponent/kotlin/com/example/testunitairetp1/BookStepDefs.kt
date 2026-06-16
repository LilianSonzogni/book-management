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
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus

class BookStepDefs {

    @LocalServerPort
    private var port: Int = 0

    private lateinit var booksResponse: Response

    @Before
    fun setup() {
        RestAssured.baseURI = "http://localhost:$port"
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
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
            """{"title":"${row["title"]}","author":"${row["author"]}"}"""
        }
        booksResponse.body().jsonPath().prettify() shouldBe JsonPath(expectedJson).prettify()
    }
}
