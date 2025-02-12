package com.example.achievera.data.reposotory

import com.example.achievera.data.dao.linkedimagesDao
import com.example.achievera.data.model.LinkedImage
import com.example.achievera.domain.repositoryInterfaces.ILinkedImagesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LinkedImagesRepository @Inject constructor(
    private val linkedImagesDao: linkedimagesDao,
): ILinkedImagesRepository {
    override fun getLinkedImages(noteId: Long): Flow<List<LinkedImage>> {
        return linkedImagesDao.getLinkedImages(noteId)
    }

    override suspend fun insertLinkedImage(linkedImage: LinkedImage) {
        linkedImagesDao.insertLinkedImage(linkedImage)
    }

    override suspend fun deleteLinkedImage(linkedImage: LinkedImage) {
        linkedImagesDao.deleteLinkedImage(linkedImage)
    }
    override suspend fun deleteLinkedImagesByNoteId(noteId: Long) {
        linkedImagesDao.deleteLinkedImagesByNoteId(noteId)
    }
}