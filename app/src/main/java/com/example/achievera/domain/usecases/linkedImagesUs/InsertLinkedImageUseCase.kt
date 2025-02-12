package com.example.achievera.domain.usecases.linkedImagesUs

import com.example.achievera.data.model.LinkedImage
import com.example.achievera.domain.repositoryInterfaces.ILinkedImagesRepository
import javax.inject.Inject

class InsertLinkedImageUseCase @Inject constructor(private val LinkImagesRepository: ILinkedImagesRepository) {
    suspend operator fun invoke(id:Long,noteId:Long,imageUrl:String){
        return LinkImagesRepository.insertLinkedImage(LinkedImage(id, noteId,imageUrl))
    }
}