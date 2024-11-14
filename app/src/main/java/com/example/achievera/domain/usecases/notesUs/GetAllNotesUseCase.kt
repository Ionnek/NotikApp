package com.example.achievera.domain.usecases.notesUs

import com.example.achievera.data.model.NotesDatabaseElement
import com.example.achievera.domain.repositoryInterfaces.INotesRepository
import javax.inject.Inject

class GetAllNotesUseCase @Inject constructor(private val notesRepository: INotesRepository) {
    suspend operator fun invoke(): List<NotesDatabaseElement> {
        return notesRepository.getAllNotes()
    }
}