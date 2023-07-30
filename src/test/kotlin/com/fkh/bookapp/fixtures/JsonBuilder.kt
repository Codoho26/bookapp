package com.fkh.bookapp.fixtures

import com.fkh.bookapp.infrastructure.adapters.input.graphql.RequestBookDto
import com.fkh.bookapp.model.Book

fun Book.toJson() = """
{
    "id": "${id.value}",
    "name": "$name",
    "pageCount": $pageCount,
    "author":  {
        "id": "${author.id.value}",
        "firstName": "${author.firstName}",
        "lastName": "${author.lastName}"
    }
}
"""

fun RequestBookDto.toJson() = """
    {
    "name": "$name",
    "pageCount": $pageCount,
    "author":  {
        "id": "${author.id}",
        "firstName": "${author.firstName}",
        "lastName": "${author.lastName}"
    }
}
"""