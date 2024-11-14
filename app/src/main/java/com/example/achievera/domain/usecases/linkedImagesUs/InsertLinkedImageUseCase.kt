package com.example.achievera.domain.usecases.linkedImagesUs

import com.example.achievera.data.model.LinkedImage
import com.example.achievera.domain.repositoryInterfaces.ILinkedImagesRepository
import javax.inject.Inject

class InsertLinkedImageUseCase @Inject constructor(private val LinkImagesRepository: ILinkedImagesRepository) {
    suspend operator fun invoke(insertedLinkedImage: LinkedImage){
        return LinkImagesRepository.insertLinkedImage(insertedLinkedImage)
    }
}