/*
 * Copyright 2025 Ivan Borzykh
 * SPDX-License-Identifier: MIT
 */
package com.borzykh.achievera.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.borzykh.achievera.data.model.NoteTagCrossRef
import com.borzykh.achievera.data.model.NotesDatabaseElement
import com.borzykh.achievera.data.model.Tag
import kotlinx.coroutines.flow.Flow

@Dao
interface tagsDao {
    @Query("SELECT * FROM tags")
    fun getAllTags(): Flow<List<Tag>>
    @Query("SELECT * FROM tags WHERE id = :id")
    suspend fun getTagById(id: Long): Tag
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTag(tag: Tag): Long
    @Query("UPDATE tags SET name = :name, color = :color WHERE id = :id")
    suspend fun updateTag(id: Long, name: String, color: String)
    @Delete
    suspend fun deleteTag(tab: Tag)
    @Query("SELECT * FROM tags WHERE id IN (SELECT tagId FROM NoteTagCrossRef WHERE noteId = :noteId)")
     fun getCurrentNoteTags(noteId: Long): Flow<List<Tag>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrentNoteTag(TagConnection: NoteTagCrossRef)
    @Delete
    suspend fun deleteCurrentNoteTag(TagConnection: NoteTagCrossRef)
    @Query("SELECT * FROM notes WHERE id IN (SELECT noteId FROM NoteTagCrossRef WHERE tagId = :tagId)")
    fun getNotesByTag(tagId: Long): Flow<List<NotesDatabaseElement>>

}