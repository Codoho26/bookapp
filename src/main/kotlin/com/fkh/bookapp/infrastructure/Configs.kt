package com.fkh.bookapp.infrastructure

import com.fkh.bookapp.application.service.BookService
import com.fkh.bookapp.infrastructure.adapters.output.db.BookPostgresqlRepository
import com.fkh.bookapp.model.Author
import com.fkh.bookapp.model.AuthorId
import com.fkh.bookapp.model.ports.AuthorFinder
import com.fkh.bookapp.model.ports.BookStore
import java.util.UUID
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.graphql.execution.RuntimeWiringConfigurer
import org.springframework.jdbc.core.JdbcTemplate

@Configuration
class Configs {

    @Bean
    fun fetchBookService(
        bookStore: BookStore,
    ) = BookService(
        bookStore = bookStore,
        generateId = { UUID.randomUUID() }
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
    fun bookStore(
        jdbcTemplate: JdbcTemplate
    ) = BookPostgresqlRepository(
        jdbcTemplate
    )

//        object : BookStore {
//        override fun find(id: BookId): Either<BookNotFoundError, Book> = when (id.value.toString()) {
//            "f0249d34-945c-4b8d-9e68-6bafb6fae7e0" -> {
//                Book(
//                    id = id,
//                    name = "Introduction to Graphql",
//                    pageCount = 365,
//                    author = Author(
//                        id = AuthorId(UUID.fromString("01732888-e065-4b0a-b41c-59b586a131a1")),
//                        firstName = "Faraz",
//                        lastName = "Khatami",
//                    )
//                ).right()
//            }
//            else -> BookNotFoundError.left()
//        }
//    }

    @Bean
    fun runtimeWiringConfigurer(): RuntimeWiringConfigurer = RuntimeWiringConfigurer { builder ->
        builder.apply { }
    }
}