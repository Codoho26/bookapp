package com.fkh.bookapp.infrastructure.adapters.input.graphql

import com.fkh.bookapp.model.AuthorNotFoundError
import com.fkh.bookapp.model.BookNotFoundError
import com.fkh.bookapp.model.DomainError
import graphql.GraphQLError
import graphql.GraphqlErrorBuilder
import graphql.GraphqlErrorException
import graphql.schema.DataFetchingEnvironment
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter
import org.springframework.graphql.execution.ErrorType.INTERNAL_ERROR
import org.springframework.graphql.execution.ErrorType.NOT_FOUND
import org.springframework.web.bind.annotation.ControllerAdvice

@ControllerAdvice
class GraphqlExceptionResolver: DataFetcherExceptionResolverAdapter() {
    override fun resolveToSingleError(throwable: Throwable, env: DataFetchingEnvironment): GraphQLError = when (throwable) {
        is GraphqlErrorException -> throwable.let {
            GraphqlErrorBuilder.newError(env).errorType(it.errorType).message(it.message).build()
        }
        else -> GraphqlErrorBuilder.newError(env).errorType(INTERNAL_ERROR).message("Internal server error!").build()
    }
}

fun DomainError.throwException(): Nothing = when (this) {
    BookNotFoundError -> throw bookNotFound
    AuthorNotFoundError -> TODO()
}

private val bookNotFound = GraphqlErrorException.newErrorException()
    .message("Book not found!")
    .errorClassification(NOT_FOUND)
    .build()
