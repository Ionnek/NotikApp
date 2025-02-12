package com.example.achievera.domain.usecases.tagsUs

import com.example.achievera.data.model.NoteTagCrossRef
import com.example.achievera.data.model.NotesDatabaseElement
import com.example.achievera.data.model.Tag
import com.example.achievera.domain.repositoryInterfaces.ITagsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteTagUseCase @Inject constructor(private val tagRepository: ITagsRepository) {
    suspend operator fun invoke(tag: Tag) {
        return tagRepository.deleteTag(tag)
    }
}
class GetAllTagsUseCase @Inject constructor(private val tagRepository: ITagsRepository) {
    operator fun invoke(): Flow<List<Tag>> {
        return tagRepository.getAllTags()
    }
}
class GetTagByIdUseCase @Inject constructor(private val tagRepository: ITagsRepository) {
    suspend operator fun invoke(tagId: Long):Tag {
        return tagRepository.getTagById(tagId)
    }
}
class InsertTagUseCase @Inject constructor(private val tagRepository: ITagsRepository) {
    suspend operator fun invoke(Name: String, Color: String, IsActived: Boolean):Long{
        return tagRepository.insertTag(Tag(0, Name, Color, IsActived))
    }
}
class UpdateTagUseCase @Inject constructor(private val tagRepository: ITagsRepository) {
    suspend operator fun invoke(tag: Tag) {
        return tagRepository.updateTag(tag)
    }
}
class GetCurrentNoteTagsUseCase @Inject constructor(private val tagRepository: ITagsRepository) {
     operator fun invoke(NoteId: Long): Flow<List<Tag>> {
        return tagRepository.getCurrentNoteTags(NoteId)
    }}
class InsertCurrentNoteTagUseCase @Inject constructor(private val tagRepository: ITagsRepository) {
    suspend operator fun invoke(noteId: Long, tagId: Long) {
        return tagRepository.insertCurrentNoteTag(NoteTagCrossRef(noteId,tagId))
    }
}
class DeleteCurrentNoteTagUseCase @Inject constructor(private val tagRepository: ITagsRepository) {
    suspend operator fun invoke(noteId: Long, tagId: Long) {
        return tagRepository.deleteCurrentNoteTag(NoteTagCrossRef(noteId,tagId))
    }
}
class GetNotesByTagUseCase @Inject constructor(private val tagRepository: ITagsRepository) {
    operator fun invoke(tagId: Long): Flow<List<NotesDatabaseElement>> {
        return tagRepository.getNotesByTag(tagId)
    }
}
