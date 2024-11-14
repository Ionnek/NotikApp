package com.example.achievera.domain.repositoryInterfaces

import com.example.achievera.data.model.Tag

interface ITagsRepository {
    suspend fun getAllTags(): List<Tag>
    suspend fun insertTag(tag: Tag)
    suspend fun deleteTag(tag: Tag)
    suspend fun updateTag(tag: Tag)
    suspend fun getTagById(tag: Long): Tag
}