/*
 * Copyright 2025 Ivan Borzykh
 * SPDX-License-Identifier: MIT
 */
package com.borzykh.achievera.ui.viewModel

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.borzykh.achievera.data.model.LinkedImage
import com.borzykh.achievera.data.model.NotesDatabaseElement
import com.borzykh.achievera.data.model.Tag
import com.borzykh.achievera.domain.usecases.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn

import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesListViewModel @Inject constructor(
    private val insertNoteUseCase: InsertNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val editNoteUseCase: EditNoteUseCase,
    private val searchQueryUseCase: GetSearchQueryUseCase,
    private val InsertTagUseCase: InsertTagUseCase,
    private val DeleteTagUseCase: DeleteTagUseCase,
    private val GetAllTagsUseCase: GetAllTagsUseCase,
    private val GetCurrentNoteTagsUseCase: GetCurrentNoteTagsUseCase,
    private val InsertCurrentNoteTagUseCase: InsertCurrentNoteTagUseCase,
    private val DeleteCurrentNoteTagUseCase: DeleteCurrentNoteTagUseCase,
    private val insertLinkedImageUseCase: InsertLinkedImageUseCase,
    private val DeleteLinkedImageUseCase: DeleteLinkedImageUseCase,
    private val GetLinkedImagesUseCase: GetLinkedImagesUseCase,
    private val DeleteLinkedImagesByNoteIdUseCase: DeleteLinkedImagesByNoteIdUseCase,
    private val GetNoteByIdUseCase:GetNoteByIdUseCase,
    private val updateTagUseCase: UpdateTagUseCase,
    private val GetNotesByTagUseCase: GetNotesByTagUseCase,
    private val sharedPreferences: SharedPreferences
) : ViewModel(),INotesListViewModel {
    init {
        GettAllTags()
        updateTagsToActiveFilter()
    }

    override var MainListType: Int = sharedPreferences.getInt("list_type", 1)
    override var columnCountForMainList: Int = sharedPreferences.getInt("column_count", 2)

    override fun setColumnCount(value: Int) {
        columnCountForMainList = value
        sharedPreferences.edit()
            .putInt("column_count", value)
            .apply()
    }
    override fun setListType(value: Int) {
        MainListType = value
        sharedPreferences.edit()
            .putInt("list_type", value)
            .apply()
    }
    override var MenoState=1

    private val _images = MutableStateFlow<List<LinkedImage>>(emptyList())
    override val images: StateFlow<List<LinkedImage>> get() = _images

    private val _Tags = MutableStateFlow<List<Tag>>(emptyList())
    override val tags: StateFlow<List<Tag>> get() = _Tags

    private val _FilterTags = MutableStateFlow<List<Tag>>(emptyList())
    override val FilterTags: StateFlow<List<Tag>> get() = _FilterTags

    private val _NoteTags = MutableStateFlow<List<Tag>>(emptyList())
    override val NoteTags: StateFlow<List<Tag>> get() = _NoteTags

    private val _note = MutableLiveData<NotesDatabaseElement>()
    override val note: LiveData<NotesDatabaseElement> get() = _note

    private val currentQuery = MutableStateFlow("")
    private val QueryTagFilters = MutableStateFlow<Set<Long>>(emptySet())

    private val _insertedNoteId = MutableStateFlow<Long>(0)
    override val insertedNoteId: StateFlow<Long> get() = _insertedNoteId

    private val _eventFlow = MutableSharedFlow<NotesListEvent>(replay = 0)
    override val eventFlow = _eventFlow.asSharedFlow()

    override val testImageList = emptyList<Int>()
    override fun getNotesByTag(tagId: Long): Flow<List<NotesDatabaseElement>> {
        return GetNotesByTagUseCase(tagId).flowOn(Dispatchers.IO)
    }
    override fun addTagToQuery(tagId: Long) {
        QueryTagFilters.value += tagId
        updateTagsToActiveFilter()
    }
    override fun removeTagFromQuery(tagId: Long) {
        QueryTagFilters.value -= tagId
        updateTagsToActiveFilter()
    }
    override fun updateTagsToActiveFilter() {
        viewModelScope.launch {
            try {
                val allTags = GetAllTagsUseCase().first()

                val updatedFilterTags = allTags.map { tag ->
                    tag.copy(isActived = QueryTagFilters.value.contains(tag.id))
                }

                _FilterTags.value = updatedFilterTags

                Log.d("NotesListViewModel", "Updated tags: $updatedFilterTags")
            } catch (e: Exception) {
                Log.e("NotesListViewModel", "error updating tags: ${e.message}")
            }
        }
    }
    override fun setQuery(query: String,) {
        currentQuery.value = query
    }

    override val notes: Flow<PagingData<NotesDatabaseElement>> =
        combine(currentQuery, QueryTagFilters) { query, tagIds ->
            Pair(query, tagIds)
        }.flatMapLatest { (query, tagIds) ->
            searchQueryUseCase(query, tagIds)
        }.cachedIn(viewModelScope)

    override fun getNoteById(id: Long) {
        viewModelScope.launch() {
            _note.value = GetNoteByIdUseCase(id)
        }
    }

    override fun insertNote(name: String, date: Long, dateEdited: String, text: String, reminder: Long, color: String, isHandwritten: Boolean,checkboxes:String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val newNote = NotesDatabaseElement(
                    id = 0, // Предполагается, что ID генерируется автоматически
                    name = name,
                    date = date,
                    dateEdited = dateEdited,
                    text = text,
                    reminder = reminder,
                    color = color,
                    isHandwritten = isHandwritten,
                    chekboxes = checkboxes
                )
                val newId = insertNoteUseCase(newNote)
                Log.d("NotesListViewModel", "Создалась новая пустая заметка с id: $newId")
                _eventFlow.emit(NotesListEvent.NoteInserted(newId))
                // Другие действия, например, обновление списков
            } catch (e: Exception) {
                Log.d("NotesListViewModel", "Не Создалась новая пустая заметка ")
            }
            setQuery("")
        }
    }
    override fun deleteNote(note: NotesDatabaseElement) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                        deleteNoteUseCase(note.id)
                        setQuery("")
            } catch (e: Exception) {
                Log.e("NotesListViewModel", "Ошибка при удалении заметки: ${e.message}")
            }
        }
    }
    override fun updateNote(id:Long, name: String, date: Long, dateEdited: String, text: String, reminder: Long, color: String, isHandwritten: Boolean,checkboxes: String) {
        viewModelScope.launch(Dispatchers.IO) {
            var n1= NotesDatabaseElement(id, name, date, dateEdited, text, reminder, color, isHandwritten, chekboxes = checkboxes)
            editNoteUseCase(n1)
            setQuery("")
        }
    }
    override fun InsertStandaloneTag(name: String, color: String, isActived: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            InsertTagUseCase(name,color,isActived)
            updateTagsToActiveFilter()
        }
    }
    override fun updateTag(tag: Tag,name: String,color: String){
        viewModelScope.launch(Dispatchers.IO) {
            updateTagUseCase(tag.copy(name = name, color = color))
        }
    }
    override fun InsertTag(NoteId:Long,name: String, color: String, isActived: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            var id=InsertTagUseCase(name,color,isActived)
            InsertCurrentNoteTag(NoteId,id)
            updateTagsToActiveFilter()
        }
    }
    override fun DeleteTag(tag:Tag) {
        viewModelScope.launch(Dispatchers.IO) {
            DeleteTagUseCase(tag)
            updateTagsToActiveFilter()
        }
    }
    override fun GettAllTags(){
        viewModelScope.launch(Dispatchers.IO) {
            GetAllTagsUseCase().collect { tagsList ->
                _Tags.value = tagsList
            }
        }
    }
    override fun InsertCurrentNoteTag(noteId: Long, tagId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("NotesListViewModel", "тег в Заметку вставляется: ${noteId},${tagId}")
            InsertCurrentNoteTagUseCase(noteId, tagId)
            updateActiveTags(noteId)
        }
    }

    override fun DeleteCurrentNoteTag(noteId: Long, tagId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("NotesListViewModel", "тег из Заметки удаляется: ${noteId},${tagId}")
            DeleteCurrentNoteTagUseCase(noteId, tagId)
            updateActiveTags(noteId)
        }
    }

    override fun updateActiveTags(noteId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            combine(
                GetAllTagsUseCase(),
                GetCurrentNoteTagsUseCase(noteId)
            ) { allTags, noteTags ->
                val updatedTags = allTags.map { tag ->
                    if (noteTags.contains(tag)) {
                        tag.copy(isActived = true)
                    } else {
                        tag.copy(isActived = false)
                    }
                }
                _NoteTags.value = updatedTags
                Log.d("NotesListViewModel", "tag deliting from note: ${updatedTags.toString()}")
            }.collect({})
        }
    }

    override fun InsertNewPhoto(id: Long, noteId: Long, imageUrl: String) {
        viewModelScope.launch(Dispatchers.IO) {
            insertLinkedImageUseCase(id, noteId, imageUrl)
        }
    }

    override fun DeletePhoto(id: Long, noteId: Long, imageUrl: String) {
        viewModelScope.launch(Dispatchers.IO) {
            DeleteLinkedImageUseCase(id, noteId, imageUrl)
        }
    }
    override fun GetNotePhotos(noteId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            GetLinkedImagesUseCase(noteId).collect { photoList ->
                _images.value = photoList
            }
        }
    }

    override fun DeleteAllPhotos(noteId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            DeleteLinkedImagesByNoteIdUseCase(noteId)
        }
    }
}

sealed class NotesListEvent {
    data class NoteInserted(val noteId: Long) : NotesListEvent()
}