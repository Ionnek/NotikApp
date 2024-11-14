package com.example.achievera.domain.usecases.linkedImagesUs

import com.example.achievera.domain.repositoryInterfaces.ILinkedImagesRepository
import javax.inject.Inject

class DeleteLinkedImagesByNoteIdUseCase @Inject constructor(private val LinkImagesRepository: ILinkedImagesRepository) {
    suspend operator fun invoke(NoteId: Long) {
        return LinkImagesRepository.deleteLinkedImagesByNoteId(NoteId)
    }
}