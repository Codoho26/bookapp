package com.fkh.bookapp.model

import java.util.UUID

data class Book(
    val id: BookId,
    val name: String,
    val pageCount: Int?,
    val author: Author,
)

data class BookId(val value: UUID)

data class Author(
    val id: AuthorId,
    val firstName: String?,
    val lastName: String?,
)

data class AuthorId(val value: UUID)
