/*
 * Copyright 2025 Ivan Borzykh
 * SPDX-License-Identifier: MIT
 */
package com.borzykh.achievera.domain.repositoryInterfaces

import com.borzykh.achievera.data.model.NoteTagCrossRef
import com.borzykh.achievera.data.model.NotesDatabaseElement
import com.borzykh.achievera.data.model.Tag
import kotlinx.coroutines.flow.Flow

interface ITagsRepository {
    fun getAllTags(): Flow<List<Tag>>
    fun getCurrentNoteTags(NoteId: Long): Flow<List<Tag>>
    suspend fun insertTag(tag: Tag): Long
    suspend fun deleteTag(tag: Tag)
    suspend fun updateTag(tag: Tag)
    suspend fun getTagById(tag: Long): Tag
    suspend fun insertCurrentNoteTag(TagConnection: NoteTagCrossRef)
    suspend fun deleteCurrentNoteTag(TagConnection: NoteTagCrossRef)
    fun getNotesByTag(tagId: Long): Flow<List<NotesDatabaseElement>>

}