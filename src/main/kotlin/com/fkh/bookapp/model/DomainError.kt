package com.fkh.bookapp.model

sealed interface DomainError

object BookNotFoundError: DomainError

object AuthorNotFoundError: DomainError