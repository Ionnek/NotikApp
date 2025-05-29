/*
 * Copyright 2025 Ivan Borzykh
 * SPDX-License-Identifier: MIT
 */
package com.borzykh.achievera.domain.usecases

import android.util.Log
import androidx.paging.PagingData
import com.borzykh.achievera.data.model.NotesDatabaseElement
import com.borzykh.achievera.domain.repositoryInterfaces.INotesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UnlinkNotesUseCase @Inject constructor(private val notesRepository: INotesRepository) {
    suspend operator fun invoke(noteId: Long, linkedNote: Long) {
        return notesRepository.unlinkNotes(noteId,linkedNote)
    }
}

class LinkNotesUseCase @Inject constructor(private val notesRepository: INotesRepository) {
    suspend operator fun invoke(noteId: Long, linkedNote: Long) {
        return notesRepository.linkNotes(noteId,linkedNote)
    }
}
class InsertNoteUseCase @Inject constructor(private val notesRepository: INotesRepository) {
    suspend operator fun invoke(note: NotesDatabaseElement):Long {
        return notesRepository.insertNote(note)
    }
}
class GetSearchQueryUseCase @Inject constructor(private val notesRepository: INotesRepository) {
    operator fun invoke(query: String,Ids:Set<Long>): Flow<PagingData<NotesDatabaseElement>> {
        Log.d("NotesListViewModel", "Notes finded: ${notesRepository.getSearchQuery(query,Ids)}")
        return notesRepository.getSearchQuery(query,Ids)
    }
}
class GetNoteByIdUseCase @Inject constructor(private val NotesRepository: INotesRepository) {
    suspend operator fun invoke(NoteId: Long): NotesDatabaseElement {
        return NotesRepository.getNoteByID(NoteId)
    }
}
class GetLinkedNotesUseCase @Inject constructor(private val notesRepository: INotesRepository) {
    suspend operator fun invoke(noteId: Long): List<NotesDatabaseElement> {
        return notesRepository.getLinkedNotes(noteId)
    }
}
class GetAllNotesUseCase @Inject constructor(private val notesRepository: INotesRepository) {
    suspend operator fun invoke(): List<NotesDatabaseElement> {
        return notesRepository.getAllNotes()
    }
}
class EditNoteUseCase @Inject constructor(private val notesRepository: INotesRepository) {
    suspend operator fun invoke(note: NotesDatabaseElement) {
        return notesRepository.editNote(note)
    }
}
class DeleteNoteUseCase @Inject constructor(private val notesRepository: INotesRepository) {
    suspend operator fun invoke(noteId: Long) {
        return notesRepository.deleteNote(noteId)
    }
}