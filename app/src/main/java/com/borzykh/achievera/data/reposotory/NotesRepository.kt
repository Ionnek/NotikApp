/*
 * Copyright 2025 Ivan Borzykh
 * SPDX-License-Identifier: MIT
 */
package com.borzykh.achievera.data.reposotory

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.borzykh.achievera.data.dao.noteLinkDao
import com.borzykh.achievera.data.dao.notesDao
import com.borzykh.achievera.data.model.NoteLink
import com.borzykh.achievera.data.model.NotesDatabaseElement
import com.borzykh.achievera.domain.repositoryInterfaces.INotesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NotesRepository @Inject constructor(
    private val notesDao: notesDao,
    private val noteLinkDao: noteLinkDao
): INotesRepository {

    override suspend fun getAllNotes(): List<NotesDatabaseElement> {
        if(notesDao.getAllNotes()!=null)
        {return notesDao.getAllNotes()}
        else{return emptyList()}
    }

    override suspend fun getNoteByID(noteId: Long): NotesDatabaseElement {
        return notesDao.getNoteById(noteId)
    }
    override suspend fun insertNote(note: NotesDatabaseElement):Long {
        return notesDao.insertNote(note)
    }
    override suspend fun editNote(note: NotesDatabaseElement){
        notesDao.updateNote(note)
    }
    override suspend fun deleteNote(noteId: Long) {
        notesDao.deleteNote(noteId)

    }
    override suspend fun getLinkedNotes(noteId: Long): List<NotesDatabaseElement> {
        val linkedNoteIds = noteLinkDao.getLinkedNotes(noteId)
        return linkedNoteIds.mapNotNull { id -> notesDao.getNoteById(id) }
    }
    override suspend fun linkNotes(noteId: Long, linkedNoteId: Long) {
        noteLinkDao.insertLink(NoteLink(noteId, linkedNoteId))
    }
    override suspend fun unlinkNotes(noteId: Long, linkedNoteId: Long) {
        noteLinkDao.deleteLink(noteId, linkedNoteId)
    }
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun getSearchQuery(query: String, tagsId:Set<Long>): Flow<PagingData<NotesDatabaseElement>> {
        Log.d("NotesListViewModel", "Vizov")
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 15,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = { notesDao.searchNotes(QueryBuilder().getSearchQuery(query,tagsId))}
        ).flow
    }
}
