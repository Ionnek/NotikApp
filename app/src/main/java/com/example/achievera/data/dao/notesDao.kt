package com.example.achievera.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.achievera.data.model.NotesDatabaseElement

@Dao//аннотация для интерфейса Dao
interface notesDao{

    @Query("SELECT * FROM notes")
    suspend fun getAllNotes(): List<NotesDatabaseElement>

    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun getNoteById(id: Long): NotesDatabaseElement

    @Insert
    suspend fun insertNote(note: NotesDatabaseElement)

    @Update
    suspend fun updateNote(note: NotesDatabaseElement)

    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun deleteNote(id: Long)

    @Query("""
        SELECT * FROM notes 
        JOIN NoteTagCrossRef ON notes.id = NoteTagCrossRef.noteId
        WHERE (name LIKE '%' || :query || '%' 
           OR text LIKE '%' || :query || '%')
           AND NoteTagCrossRef.tagId = :tagId
        ORDER BY date DESC 
    """)
    fun searchNotes(query: String,tagId: Int): PagingSource<Int, NotesDatabaseElement>
}
/*
This code defines a Data Access Object (DAO) interface for interacting with a
 database table named "notes". It uses Room,
a persistence library for Android, to manage database operations.
*/