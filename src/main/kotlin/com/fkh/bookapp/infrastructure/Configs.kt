package com.fkh.bookapp.infrastructure

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.fkh.bookapp.application.service.FetchBookService
import com.fkh.bookapp.model.Author
import com.fkh.bookapp.model.AuthorId
import com.fkh.bookapp.model.Book
import com.fkh.bookapp.model.BookId
import com.fkh.bookapp.model.BookNotFoundError
import com.fkh.bookapp.model.ports.AuthorFinder
import com.fkh.bookapp.model.ports.BookStore
import java.util.UUID
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.graphql.execution.RuntimeWiringConfigurer

@Configuration
class Configs {

    @Bean
    fun fetchBookService(
        bookStore: BookStore,
    ) = FetchBookService(
        bookStore = bookStore
    )

    @Bean
    fun authorFinder() = object : AuthorFinder {
        override fun find(id: AuthorId): Author = Author(
            id = id,
            firstName = "Faraz",
            lastName = "Khatami",
        )
    }

    @Bean
    fun bookStore() = object : BookStore {
        override fun find(id: BookId): Either<BookNotFoundError, Book> = when (id.value.toString()) {
            "f0249d34-945c-4b8d-9e68-6bafb6fae7e0" -> {
                Book(
                    id = id,
                    name = "Introduction to Graphql",
                    pageCount = 365,
                    author = Author(
                        id = AuthorId(UUID.fromString("01732888-e065-4b0a-b41c-59b586a131a1")),
                        firstName = "Faraz",
                        lastName = "Khatami",
                    )
                ).right()
            }
            else -> BookNotFoundError.left()
        }
    }

    @Bean
    fun runtimeWiringConfigurer(): RuntimeWiringConfigurer = RuntimeWiringConfigurer {
        builder -> builder.apply {  }
    }
}