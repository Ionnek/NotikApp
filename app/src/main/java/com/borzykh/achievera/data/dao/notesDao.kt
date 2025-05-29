/*
 * Copyright 2025 Ivan Borzykh
 * SPDX-License-Identifier: MIT
 */
package com.borzykh.achievera.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteQuery
import com.borzykh.achievera.data.model.NotesDatabaseElement

@Dao
interface notesDao{

    @Query("SELECT * FROM notes")
    suspend fun getAllNotes(): List<NotesDatabaseElement>

    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun getNoteById(id: Long): NotesDatabaseElement

    @Insert
    suspend fun insertNote(note: NotesDatabaseElement):Long

    @Update
    suspend fun updateNote(note: NotesDatabaseElement)

    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun deleteNote(id: Long)

    @RawQuery(observedEntities = [NotesDatabaseElement::class])
    fun searchNotes(query: SupportSQLiteQuery): PagingSource<Int, NotesDatabaseElement>

}