package com.example.achievera.domain.usecases.linkedImagesUs

import com.example.achievera.data.model.LinkedImage
import com.example.achievera.domain.repositoryInterfaces.ILinkedImagesRepository
import javax.inject.Inject

class DeleteLinkedImageUseCase @Inject constructor(private val LinkImagesRepository: ILinkedImagesRepository) {
    suspend operator fun invoke(DeletedLinkedImage: LinkedImage) {
        return LinkImagesRepository.deleteLinkedImage(DeletedLinkedImage)
    }
}