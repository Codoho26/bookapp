package com.fkh.bookapp.application.service

import arrow.core.Either
import com.fkh.bookapp.model.Book
import com.fkh.bookapp.model.BookId
import com.fkh.bookapp.model.BookNotFoundError
import com.fkh.bookapp.model.ports.BookStore
import java.util.UUID

class FetchBookService(
    private val bookStore: BookStore,
) {

    operator fun invoke(id: String): Either<BookNotFoundError, Book> = BookId(UUID.fromString(id))
        .let {
            bookStore.find(it)
        }
}