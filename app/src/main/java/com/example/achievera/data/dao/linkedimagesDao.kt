package com.example.achievera.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.achievera.data.model.LinkedImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Dao
interface linkedimagesDao
{
    @Query("SELECT * FROM linked_images WHERE note_id = :noteId")
    fun getLinkedImages(noteId: Long): Flow<List<LinkedImage>>

    @Insert
    suspend fun insertLinkedImage(linkedImage: LinkedImage)

    @Delete
    suspend fun deleteLinkedImage(linkedImage: LinkedImage)

    @Query("DELETE FROM linked_images WHERE note_id = :noteId")
    suspend fun deleteLinkedImagesByNoteId(noteId: Long)
}