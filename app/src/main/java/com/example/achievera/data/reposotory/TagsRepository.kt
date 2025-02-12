package com.example.achievera.data.reposotory

import com.example.achievera.data.dao.tagsDao
import com.example.achievera.data.model.NoteTagCrossRef
import com.example.achievera.data.model.NotesDatabaseElement
import com.example.achievera.data.model.Tag
import com.example.achievera.domain.repositoryInterfaces.ITagsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TagsRepository @Inject constructor(
    private val tagsDao: tagsDao,
): ITagsRepository {
    override fun getAllTags(): Flow<List<Tag>> {
        return tagsDao.getAllTags()
    }

    override suspend fun insertTag(tag: Tag): Long {
        return tagsDao.insertTag(tag)
    }

    override suspend fun deleteTag(tag: Tag) {
        tagsDao.deleteTag(tag)
    }

    override suspend fun updateTag(tag: Tag) {
        tagsDao.updateTag(tag.id, tag.name, tag.color,)
    }

    override suspend fun getTagById(tag: Long): Tag {
        return tagsDao.getTagById(tag)
    }

    override fun getCurrentNoteTags(NoteId: Long): Flow<List<Tag>> {
        return tagsDao.getCurrentNoteTags(NoteId)
    }

    override suspend fun insertCurrentNoteTag(TagConnection: NoteTagCrossRef) {
        return tagsDao.insertCurrentNoteTag(TagConnection)
    }

    override suspend fun deleteCurrentNoteTag(TagConnection: NoteTagCrossRef) {
        return tagsDao.deleteCurrentNoteTag(TagConnection)
    }
    override fun getNotesByTag(tagId: Long): Flow<List<NotesDatabaseElement>> {
        return tagsDao.getNotesByTag(tagId)
    }

}