/*
 * Copyright 2025 Ivan Borzykh
 * SPDX-License-Identifier: MIT
 */
package com.borzykh.achievera.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.borzykh.achievera.data.model.LinkedImage
import com.borzykh.achievera.data.model.NotesDatabaseElement
import com.borzykh.achievera.data.model.Tag
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface INotesListViewModel {
    var MenoState:Int
    var columnCountForMainList: Int

    var MainListType:Int
    val notes: Flow<PagingData<NotesDatabaseElement>>
    val note: LiveData<NotesDatabaseElement>
    val tags: StateFlow<List<Tag>>
    val NoteTags: StateFlow<List<Tag>>
    val FilterTags: StateFlow<List<Tag>>
    val insertedNoteId: StateFlow<Long>
    val eventFlow: Flow<NotesListEvent>
    val images: StateFlow<List<LinkedImage>>
    val testImageList:List<Int>
    fun getNotesByTag(tagId: Long): Flow<List<NotesDatabaseElement>>

    fun setListType(value: Int)
    fun setColumnCount(value: Int)
    fun updateTagsToActiveFilter()

    fun addTagToQuery(tagId: Long)
    fun removeTagFromQuery(tagId: Long)
    fun setQuery(query: String)
    fun getNoteById(id: Long)

    fun insertNote(
        name: String,
        date: Long,
        dateEdited: String,
        text: String,
        reminder: Long,
        color: String,
        isHandwritten: Boolean,
        chekboxes: String
    )

    fun deleteNote(note: NotesDatabaseElement)

    fun updateNote(
        id: Long,
        name: String,
        date: Long,
        dateEdited: String,
        text: String,
        reminder: Long,
        color: String,
        isHandwritten: Boolean,
        chekboxes: String
    )

    fun updateTag(tag: Tag,name: String,color: String)
    fun InsertTag(NoteId:Long,s: String, s1: String, a: Boolean)
    fun InsertStandaloneTag(name: String, color: String, isActived: Boolean)
    fun DeleteTag(tag:Tag)
    fun GettAllTags()
    fun InsertCurrentNoteTag(noteId: Long, tagId: Long)
    fun DeleteCurrentNoteTag(noteId: Long, tagId: Long)
    fun updateActiveTags(noteId: Long)
    fun GetNotePhotos(noteId:Long)
    fun InsertNewPhoto(id: Long,noteId: Long,imageUrl:String)
    fun DeletePhoto(id: Long,noteId: Long,imageUrl:String)
    fun DeleteAllPhotos(noteId:Long)
}