package com.example.achievera.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.achievera.data.model.NoteLink

@Dao
interface noteLinkDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLink(link: NoteLink)

    @Query("SELECT linked_note_id FROM note_links WHERE noteId = :noteId")
    suspend fun getLinkedNotes(noteId: Long): List<Long>

    @Query("DELETE FROM note_links WHERE noteId = :noteId AND linked_note_id = :linkedNoteId")
    suspend fun deleteLink(noteId: Long, linkedNoteId: Long)

}