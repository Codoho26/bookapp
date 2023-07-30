package com.fkh.bookapp.infrastructure.adapters.input.graphql

import com.fkh.bookapp.application.service.BookService
import com.fkh.bookapp.model.Author
import com.fkh.bookapp.model.AuthorId
import com.fkh.bookapp.model.Book
import com.fkh.bookapp.model.BookId
import java.util.UUID
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class BookController(
    val bookService: BookService
) {

    @QueryMapping
    fun fetchBook(
        @Argument id: String
    ): BookDto = BookId(UUID.fromString(id)).let {
        bookService.find(it).fold(
            ifRight = { it.toDto() }, ifLeft = { it.throwException() }
        )
    }

    @MutationMapping
    fun addBook(
        @Argument requestBook: RequestBookDto
    ): BookResponse = bookService.add(requestBook.toDomain()).let { BookResponse("Successfully added the new book!") }
}

data class BookResponse(val response: String)

data class RequestBook(
    val name: String,
    val pageCount: Int?,
    val author: Author,
)

data class RequestBookDto(
    val name: String,
    val pageCount: Int?,
    val author: AuthorDto,
) {
    fun toDomain() = RequestBook(
        name = name,
        pageCount = pageCount,
        author = author.toDomain(),
    )
}

data class BookDto(
    val id: String,
    val name: String,
    val pageCount: Int?,
    val author: AuthorDto,
)

data class AuthorDto(
    val id: String,
    val firstName: String?,
    val lastName: String?,
) {

    fun toDomain(): Author = Author(
        id = AuthorId(UUID.fromString(id)),
        firstName = firstName,
        lastName = lastName
    )
}

fun Book.toDto() = BookDto(
    id = id.value.toString(),
    name = name,
    pageCount = pageCount,
    author = AuthorDto(
        id = author.id.value.toString(),
        firstName = author.firstName,
        lastName = author.lastName
    )
)