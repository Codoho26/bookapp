package com.fkh.bookapp.infrastructure.adapters.output.db

import arrow.core.Either
import arrow.core.right
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fkh.bookapp.model.Book
import com.fkh.bookapp.model.BookId
import com.fkh.bookapp.model.BookNotFoundError
import com.fkh.bookapp.model.ports.BookStore
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.JdbcTemplate

class BookPostgresqlRepository(
    private val jdbcTemplate: JdbcTemplate
) : BookStore {

    private val defaultDBObjectMapper = jacksonObjectMapper()

    override fun find(id: BookId): Either<BookNotFoundError, Book> =
        jdbcTemplate.queryForObject("SELECT * FROM test_db WHERE id='$id'", BeanPropertyRowMapper(Book::class.java), id)!!.right()

    override fun save(book: Book) {
        jdbcTemplate.update(
            """
                INSERT INTO test_db(id, name, page_count, author) VALUES(?,?,?,?)
            """,
            book.id,
            book.name,
            book.pageCount,
            book.author,
        )
    }
}