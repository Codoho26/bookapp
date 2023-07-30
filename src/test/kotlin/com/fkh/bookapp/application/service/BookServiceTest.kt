package com.fkh.bookapp.application.service

import arrow.core.left
import arrow.core.right
import com.fkh.bookapp.infrastructure.adapters.input.graphql.RequestBook
import com.fkh.bookapp.model.Author
import com.fkh.bookapp.model.AuthorId
import com.fkh.bookapp.model.Book
import com.fkh.bookapp.model.BookId
import com.fkh.bookapp.model.BookNotFoundError
import com.fkh.bookapp.model.ports.BookStore
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.util.UUID
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class BookServiceTest {

    private val bookStore = mockk<BookStore>(relaxed = true)

    private val bookService = BookService(
        bookStore = bookStore,
        generateId = { UUID.randomUUID() }
    )

    @Nested
    inner class AddBookTests {
        @Test
        fun `should find an existing book`() {
            val bookId = BookId(UUID.fromString("f0249d34-945c-4b8d-9e68-6bafb6fae7e0"))
            val authorId = AuthorId(UUID.fromString("01732888-e065-4b0a-b41c-59b586a131a1"))
            val requestBook = RequestBook(
                name = "Introduction to Graphql",
                pageCount = 365,
                author = Author(
                    id = authorId,
                    firstName = "Faraz",
                    lastName = "Khatami"
                )
            )

            val result = bookService.add(requestBook)

            verify { bookStore.save(any()) }
        }

    }

    @Nested
    inner class FetchBookTests {
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
            every { bookStore.find(bookId) } returns book.right()

            val result = bookService.find(bookId)

            assertThat(result).isEqualTo(book.right())
        }

        @Test
        fun `should fail when fetching book fails for any reason`() {
            val bookId = BookId(UUID.randomUUID())
            every { bookStore.find(bookId) } returns BookNotFoundError.left()

            val result = bookService.find(bookId)

            assertThat(result).isEqualTo(BookNotFoundError.left())
        }
    }
}
