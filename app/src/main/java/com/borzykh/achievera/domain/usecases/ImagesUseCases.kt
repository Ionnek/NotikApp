/*
 * Copyright 2025 Ivan Borzykh
 * SPDX-License-Identifier: MIT
 */
package com.borzykh.achievera.domain.usecases

import com.borzykh.achievera.data.model.LinkedImage
import com.borzykh.achievera.domain.repositoryInterfaces.ILinkedImagesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteLinkedImagesByNoteIdUseCase @Inject constructor(private val LinkImagesRepository: ILinkedImagesRepository) {
    suspend operator fun invoke(NoteId: Long) {
        return LinkImagesRepository.deleteLinkedImagesByNoteId(NoteId)
    }
}
class DeleteLinkedImageUseCase @Inject constructor(private val LinkImagesRepository: ILinkedImagesRepository) {
    suspend operator fun invoke(id:Long,noteId:Long,imageUrl:String) {
        return LinkImagesRepository.deleteLinkedImage(LinkedImage(id,noteId,imageUrl))
    }
}
class GetLinkedImagesUseCase @Inject constructor(private val LinkImagesRepository: ILinkedImagesRepository) {
    operator fun invoke(NoteId: Long): Flow<List<LinkedImage>> {
        return LinkImagesRepository.getLinkedImages(NoteId)
    }
}
class InsertLinkedImageUseCase @Inject constructor(private val LinkImagesRepository: ILinkedImagesRepository) {
    suspend operator fun invoke(id:Long,noteId:Long,imageUrl:String){
        return LinkImagesRepository.insertLinkedImage(LinkedImage(id, noteId,imageUrl))
    }
}