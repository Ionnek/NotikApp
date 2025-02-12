package com.example.achievera.domain.usecases.linkedImagesUs

import com.example.achievera.data.model.LinkedImage
import com.example.achievera.domain.repositoryInterfaces.ILinkedImagesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLinkedImagesUseCase @Inject constructor(private val LinkImagesRepository: ILinkedImagesRepository) {
    operator fun invoke(NoteId: Long): Flow<List<LinkedImage>> {
        return LinkImagesRepository.getLinkedImages(NoteId)
    }
}