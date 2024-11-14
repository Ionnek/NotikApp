package com.example.achievera.domain.usecases.notesUs

import com.example.achievera.domain.repositoryInterfaces.INotesRepository
import javax.inject.Inject

class UnlinkNotesUseCase @Inject constructor(private val notesRepository: INotesRepository) {
    suspend operator fun invoke(noteId: Long, linkedNote: Long) {
        return notesRepository.unlinkNotes(noteId,linkedNote)
    }
}