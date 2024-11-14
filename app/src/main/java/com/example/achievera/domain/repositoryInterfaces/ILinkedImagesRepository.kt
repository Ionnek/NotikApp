package com.example.achievera.domain.repositoryInterfaces

import com.example.achievera.data.model.LinkedImage

interface ILinkedImagesRepository {
    suspend fun getLinkedImages(noteId: Long): List<LinkedImage>
    suspend fun insertLinkedImage(linkedImage: LinkedImage)
    suspend fun deleteLinkedImage(linkedImage: LinkedImage)
    suspend fun deleteLinkedImagesByNoteId(noteId: Long)
}