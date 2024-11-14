package com.example.achievera.domain.usecases.tagsUs

import com.example.achievera.data.model.Tag
import com.example.achievera.domain.repositoryInterfaces.ITagsRepository
import javax.inject.Inject

class UpdateTagUseCase @Inject constructor(private val tagRepository: ITagsRepository) {
    suspend operator fun invoke(tag: Tag) {
        return tagRepository.updateTag(tag)
    }
}