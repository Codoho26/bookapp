package com.fkh.bookapp.model.ports

import arrow.core.Either
import com.fkh.bookapp.model.Book
import com.fkh.bookapp.model.BookId
import com.fkh.bookapp.model.BookNotFoundError

interface BookStore {

    fun find(id: BookId): Either<BookNotFoundError, Book>
}