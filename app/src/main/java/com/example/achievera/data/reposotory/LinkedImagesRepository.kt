package com.example.achievera.data.reposotory

import com.example.achievera.data.dao.linkedimagesDao
import com.example.achievera.data.model.LinkedImage
import com.example.achievera.domain.repositoryInterfaces.ILinkedImagesRepository
import javax.inject.Inject

class LinkedImagesRepository @Inject constructor(
    private val linkedImagesDao: linkedimagesDao,
): ILinkedImagesRepository {
    override suspend fun getLinkedImages(noteId: Long): List<LinkedImage> {
        return linkedImagesDao.getLinkedImages(noteId)
    }

    override suspend fun insertLinkedImage(linkedImage: LinkedImage) {
        linkedImagesDao.insertLinkedImage(linkedImage)
    }

    override suspend fun deleteLinkedImage(linkedImage: LinkedImage) {
        linkedImagesDao.deleteLinkedImage(linkedImage.id)
    }
    override suspend fun deleteLinkedImagesByNoteId(noteId: Long) {
        linkedImagesDao.deleteLinkedImagesByNoteId(noteId)
    }
}