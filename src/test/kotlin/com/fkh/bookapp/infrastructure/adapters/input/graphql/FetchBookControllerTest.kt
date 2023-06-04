package com.fkh.bookapp.infrastructure.adapters.input.graphql

import arrow.core.left
import arrow.core.right
import com.fkh.bookapp.application.service.FetchBookService
import com.fkh.bookapp.model.Author
import com.fkh.bookapp.model.AuthorId
import com.fkh.bookapp.model.Book
import com.fkh.bookapp.model.BookId
import com.fkh.bookapp.model.BookNotFoundError
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import java.util.UUID
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest
import org.springframework.context.annotation.Import
import org.springframework.graphql.test.tester.GraphQlTester

@GraphQlTest(FetchBookController::class)
@Import(GraphqlTestConfig::class)
class FetchBookControllerTest(@Autowired private val graphQlTester: GraphQlTester) {

    @MockkBean
    private lateinit var fetchBookService: FetchBookService

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
        every { fetchBookService(bookId.value.toString()) } returns book.right()

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
            .matchesJson(with(book) {
                """
                {
                    "id": "${id.value}",
                    "name": "$name",
                    "pageCount": $pageCount,
                    "author":  {
                        "id": "${author.id.value}",
                        "firstName": "${author.firstName}",
                        "lastName": "${author.lastName}"
                    }
                }
                """
            }
            )
    }

    @Test
    fun `should fail due to any domain error`() {
        val bookId = BookId(UUID.randomUUID())
        every { fetchBookService(bookId.value.toString()) } returns BookNotFoundError.left()

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
