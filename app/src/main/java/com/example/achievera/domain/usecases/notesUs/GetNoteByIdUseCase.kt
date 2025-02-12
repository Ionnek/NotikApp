package com.example.achievera.domain.usecases.notesUs

import com.example.achievera.data.model.NotesDatabaseElement
import com.example.achievera.domain.repositoryInterfaces.INotesRepository
import javax.inject.Inject

class GetNoteByIdUseCase @Inject constructor(private val NotesRepository:INotesRepository) {
    suspend operator fun invoke(NoteId: Long):NotesDatabaseElement {
        return NotesRepository.getNoteByID(NoteId)
    }
}