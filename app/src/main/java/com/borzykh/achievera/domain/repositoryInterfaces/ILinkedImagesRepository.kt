/*
 * Copyright 2025 Ivan Borzykh
 * SPDX-License-Identifier: MIT
 */
package com.borzykh.achievera.domain.repositoryInterfaces

import com.borzykh.achievera.data.model.LinkedImage
import kotlinx.coroutines.flow.Flow

interface ILinkedImagesRepository {
    fun getLinkedImages(noteId: Long): Flow<List<LinkedImage>>
    suspend fun insertLinkedImage(linkedImage: LinkedImage)
    suspend fun deleteLinkedImage(linkedImage: LinkedImage)
    suspend fun deleteLinkedImagesByNoteId(noteId: Long)
}