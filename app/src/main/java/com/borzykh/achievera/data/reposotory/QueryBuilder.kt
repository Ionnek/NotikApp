/*
 * Copyright 2025 Ivan Borzykh
 * SPDX-License-Identifier: MIT
 */
package com.borzykh.achievera.data.reposotory

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery


class QueryBuilder {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun getSearchQuery(query: String, tagsId: Set<Long>): SupportSQLiteQuery {
        val sqlBuilder = StringBuilder()
        val args = mutableListOf<Any>()

        if (tagsId.isNotEmpty()) {
            sqlBuilder.append(
                """
                SELECT n.* 
                FROM notes n 
                JOIN NoteTagCrossRef nt ON n.id = nt.noteId 
                """.trimIndent()
            )
        } else {
            sqlBuilder.append("SELECT n.* FROM notes n ")
        }

        sqlBuilder.append("WHERE n.name LIKE ? ")
        args.add("%$query%")

        if (tagsId.isNotEmpty()) {
            val placeholders = tagsId.joinToString(prefix = "(", postfix = ")", separator = ",") { "?" }
            sqlBuilder.append("AND nt.tagId IN $placeholders ")

            args.addAll(tagsId)

            sqlBuilder.append("GROUP BY n.id ")
            sqlBuilder.append("HAVING COUNT(DISTINCT nt.tagId) = ${tagsId.size} ")

        }
        sqlBuilder.append("ORDER BY n.date DESC")
        return SimpleSQLiteQuery(sqlBuilder.toString(), args.toTypedArray())
    }
}
