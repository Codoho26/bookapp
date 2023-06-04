package com.fkh.bookapp.infrastructure.adapters.input.graphql

import com.fkh.bookapp.application.service.FetchBookService
import com.fkh.bookapp.model.Book
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class FetchBookController(
    val fetchBookService: FetchBookService
) {

    @QueryMapping
    fun fetchBook(
        @Argument id: String
    ): BookDto = fetchBookService(id).fold(
        ifRight = { it.toDto() }, ifLeft = { it.throwException() }
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
)

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