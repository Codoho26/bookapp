package com.fkh.bookapp.model

import com.fkh.bookapp.infrastructure.adapters.input.graphql.RequestBook
import java.util.UUID

data class Book(
    val id: BookId,
    val name: String,
    val pageCount: Int?,
    val author: Author,
) {
    companion object {
        fun from(id: UUID, requestBook: RequestBook) = with(requestBook) {
            Book(
                id = BookId(id),
                name = name,
                pageCount = pageCount,
                author = author,
            )
        }
    }
}

data class BookId(val value: UUID)

data class Author(
    val id: AuthorId,
    val firstName: String?,
    val lastName: String?,
)

data class AuthorId(val value: UUID)
