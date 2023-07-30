package com.fkh.bookapp.application.service

import arrow.core.Either
import com.fkh.bookapp.infrastructure.adapters.input.graphql.RequestBook
import com.fkh.bookapp.model.Book
import com.fkh.bookapp.model.BookId
import com.fkh.bookapp.model.BookNotFoundError
import com.fkh.bookapp.model.ports.BookStore
import java.util.UUID

class BookService(
    private val bookStore: BookStore,
    private val generateId: () -> UUID
) {

    fun find(id: BookId): Either<BookNotFoundError, Book> =
        bookStore.find(id)

    fun add(requestBook: RequestBook): Book = Book.from(generateId(), requestBook).also {
        bookStore.save(it)
    }
}