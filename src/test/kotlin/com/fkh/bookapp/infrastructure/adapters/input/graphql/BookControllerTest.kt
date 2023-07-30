package com.fkh.bookapp.infrastructure.adapters.input.graphql

import arrow.core.left
import arrow.core.right
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fkh.bookapp.application.service.BookService
import com.fkh.bookapp.fixtures.toJson
import com.fkh.bookapp.model.Author
import com.fkh.bookapp.model.AuthorId
import com.fkh.bookapp.model.Book
import com.fkh.bookapp.model.BookId
import com.fkh.bookapp.model.BookNotFoundError
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import java.util.UUID
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest
import org.springframework.context.annotation.Import
import org.springframework.graphql.test.tester.GraphQlTester

@GraphQlTest(BookController::class)
@Import(GraphqlTestConfig::class)
class BookControllerTest(@Autowired private val graphQlTester: GraphQlTester) {

    @MockkBean
    private lateinit var bookService: BookService

    @Nested
    inner class AddBookTests {
        @Test
        fun `should add new book`() {
            val bookId = BookId(UUID.randomUUID())
            val authorId = UUID.randomUUID()
            val requestBookDto = RequestBookDto(
                name = "Introduction to Graphql",
                pageCount = 365,
                author = AuthorDto(
                    id = authorId.toString(),
                    firstName = "Faraz",
                    lastName = "Khatami"
                )
            )
            val requestBook = RequestBook(
                name = "Introduction to Graphql",
                pageCount = 365,
                author = Author(
                    id = AuthorId(authorId),
                    firstName = "Faraz",
                    lastName = "Khatami"
                )
            )
            val book = Book(
                id = bookId,
                name = "Introduction to Graphql",
                pageCount = 365,
                author = Author(
                    id = AuthorId(authorId),
                    firstName = "Faraz",
                    lastName = "Khatami"
                )
            )
            every { bookService.add(requestBook) } returns book

            val response = graphQlTester.document(
                """
                mutation(${'$'}requestBook: BookDto!) {
                    addBook(requestBook: ${'$'}requestBook) {
                       response
                    }
                }
            """
            )
                .variable(
                    "requestBook",
                    jacksonObjectMapper().readValue(requestBookDto.toJson(), Map::class.java)
                )
                .execute()

            response
                .errors().verify()
                .path("addBook")
                .matchesJson(
                    """
                       {
                            "response": "Successfully added the new book!" 
                        }
                    """
                )
        }
    }

    @Nested
    inner class FindBookTests {
        @Test
        fun `should find book by book Id`() {
            val bookId = BookId(UUID.randomUUID())
            val book = Book(
                id = bookId,
                name = "Introduction to Graphql",
                pageCount = 365,
                author = Author(
                    id = AuthorId(UUID.randomUUID()),
                    firstName = "Faraz",
                    lastName = "Khatami"
                )
            )
            every { bookService.find(bookId) } returns book.right()

            val response = graphQlTester.document(
                """
                query(${'$'}id: String!) {
                    fetchBook(id: ${'$'}id) {
                        id
                        name
                        pageCount
                        author {
                            id
                            firstName
                            lastName
                        }
                    }
                }
            """
            )
                .variable("id", bookId.value.toString())
                .execute()

            response
                .errors().verify()
                .path("fetchBook")
                .matchesJson(book.toJson())
        }

        @Test
        fun `should fail due to any domain error`() {
            val bookId = BookId(UUID.randomUUID())
            every { bookService.find(bookId) } returns BookNotFoundError.left()

            val response = graphQlTester.document(
                """
                query(${'$'}id: String!) {
                    fetchBook(id: ${'$'}id) {
                        id
                    }
                }
            """
            )
                .variable("id", bookId.value.toString())
                .execute()

            response.errors().satisfy {
                assertThat(it).hasSize(1)
                assertThat(it[0].message).isEqualTo("Book not found!")
            }
        }
    }
}
