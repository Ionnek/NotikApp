package com.example.achievera.data.reposotory

import com.example.achievera.data.dao.tagsDao
import com.example.achievera.data.model.Tag
import com.example.achievera.domain.repositoryInterfaces.ITagsRepository
import javax.inject.Inject

class TagsRepository @Inject constructor(
    private val tagsDao: tagsDao,
): ITagsRepository {
    override suspend fun getAllTags(): List<Tag> {
        return tagsDao.getAllTags()
    }

    override suspend fun insertTag(tag: Tag) {
        tagsDao.insertTag(tag.name, tag.color)
    }

    override suspend fun deleteTag(tag: Tag) {
        tagsDao.deleteTag(tag.id)
    }

    override suspend fun updateTag(tag: Tag) {
        tagsDao.updateTag(tag.id, tag.name, tag.color)
    }

    override suspend fun getTagById(tag: Long): Tag {
        return tagsDao.getTagById(tag)
    }

}