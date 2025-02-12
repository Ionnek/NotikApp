package com.example.achievera.domain.repositoryInterfaces

import com.example.achievera.data.model.NoteTagCrossRef
import com.example.achievera.data.model.NotesDatabaseElement
import com.example.achievera.data.model.Tag
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