package com.example.achievera.domain.usecases.notesUs

import com.example.achievera.data.model.NotesDatabaseElement
import com.example.achievera.domain.repositoryInterfaces.INotesRepository
import javax.inject.Inject

class GetLinkedNotesUseCase @Inject constructor(private val notesRepository: INotesRepository) {
    suspend operator fun invoke(noteId: Long): List<NotesDatabaseElement> {
        return notesRepository.getLinkedNotes(noteId)
    }
}