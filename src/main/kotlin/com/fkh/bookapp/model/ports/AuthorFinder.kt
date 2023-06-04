package com.fkh.bookapp.model.ports

import com.fkh.bookapp.model.Author
import com.fkh.bookapp.model.AuthorId

interface AuthorFinder {

    fun find(id: AuthorId): Author
}