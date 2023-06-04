package com.fkh.bookapp.application.service

import arrow.core.left
import arrow.core.right
import com.fkh.bookapp.infrastructure.Configs
import com.fkh.bookapp.model.Author
import com.fkh.bookapp.model.AuthorId
import com.fkh.bookapp.model.Book
import com.fkh.bookapp.model.BookId
import com.fkh.bookapp.model.BookNotFoundError
import java.util.UUID
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class FetchBookServiceTest {

    private val fetchBookService = FetchBookService(
        bookStore = Configs().bookStore()
    )

    @Test
    fun `should find an existing book`() {
        val bookId = BookId(UUID.fromString("f0249d34-945c-4b8d-9e68-6bafb6fae7e0"))
        val book = Book(
            id = bookId,
            name = "Introduction to Graphql",
            pageCount = 365,
            author = Author(
                id = AuthorId(UUID.fromString("01732888-e065-4b0a-b41c-59b586a131a1")),
                firstName = "Faraz",
                lastName = "Khatami",
            )
        )

        val result = fetchBookService(bookId.value.toString())

        assertThat(result).isEqualTo(book.right())
    }

    @Test
    fun `should fail when fetching book fails for any reason`() {
        val result = fetchBookService(UUID.randomUUID().toString())

        assertThat(result).isEqualTo(BookNotFoundError.left())
    }
}
