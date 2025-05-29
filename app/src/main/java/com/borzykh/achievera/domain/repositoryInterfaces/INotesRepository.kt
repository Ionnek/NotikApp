/*
 * Copyright 2025 Ivan Borzykh
 * SPDX-License-Identifier: MIT
 */
package com.borzykh.achievera.domain.repositoryInterfaces

import androidx.paging.PagingData
import com.borzykh.achievera.data.model.NotesDatabaseElement
import kotlinx.coroutines.flow.Flow

interface INotesRepository {
    suspend fun getAllNotes(): List<NotesDatabaseElement>
    suspend fun getNoteByID(noteId: Long):NotesDatabaseElement
    suspend fun insertNote(note: NotesDatabaseElement):Long
    suspend fun editNote(note: NotesDatabaseElement)
    suspend fun deleteNote(noteId: Long)
    suspend fun getLinkedNotes(noteId: Long): List<NotesDatabaseElement>
    suspend fun linkNotes(noteId: Long, linkedNoteId: Long)
    suspend fun unlinkNotes(noteId: Long, linkedNoteId: Long)
    fun getSearchQuery(query: String,tagsId:Set<Long>): Flow<PagingData<NotesDatabaseElement>>
}