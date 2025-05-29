/*
 * Copyright 2025 Ivan Borzykh
 * SPDX-License-Identifier: MIT
 */
package com.borzykh.achievera.ui.viewModel

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.borzykh.achievera.app
import com.borzykh.achievera.data.model.LinkedImage
import com.borzykh.achievera.data.model.NotesDatabaseElement
import com.borzykh.achievera.data.model.Tag
import com.borzykh.achievera.domain.usecases.*
import com.borzykh.achievera.ui.view.MyNoteWidgetProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteEditViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val sharedPreferences: SharedPreferences,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val editNoteUseCase: EditNoteUseCase,
    private val InsertTagUseCase: InsertTagUseCase,
    private val GetAllTagsUseCase: GetAllTagsUseCase,
    private val GetCurrentNoteTagsUseCase: GetCurrentNoteTagsUseCase,
    private val InsertCurrentNoteTagUseCase: InsertCurrentNoteTagUseCase,
    private val DeleteCurrentNoteTagUseCase: DeleteCurrentNoteTagUseCase,
    private val insertLinkedImageUseCase: InsertLinkedImageUseCase,
    private val DeleteLinkedImageUseCase: DeleteLinkedImageUseCase,
    private val GetLinkedImagesUseCase: GetLinkedImagesUseCase,
    private val GetNoteByIdUseCase: GetNoteByIdUseCase,
) : ViewModel(){
    init {
        GettAllTags()
    }
    var isImageActive by mutableStateOf(false)
        private set

    var NNoteId by mutableStateOf<Long>(0)
        private set

    var currentText by mutableStateOf("")
        private set

    fun onTextChange(newText: String) {
        currentText = newText
    }


    fun setNoteId(id:Long){
        NNoteId=id
    }
    private val _images = MutableStateFlow<List<LinkedImage>>(emptyList())
    val images: StateFlow<List<LinkedImage>> get() = _images

    private val _Tags = MutableStateFlow<List<Tag>>(emptyList())
    val tags: StateFlow<List<Tag>> get() = _Tags

    private val _NoteTags = MutableStateFlow<List<Tag>>(emptyList())
    val NoteTags: StateFlow<List<Tag>> get() = _NoteTags

    private val _note = MutableLiveData<NotesDatabaseElement>()
    val note: LiveData<NotesDatabaseElement> get() = _note


    fun getNoteById(id: Long) {
        viewModelScope.launch() {
            _note.value = GetNoteByIdUseCase(id)
        }
    }

    fun deleteNote(note: NotesDatabaseElement) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                deleteNoteUseCase(note.id)
            } catch (e: Exception) {
                Log.e("NotesListViewModel", "error deleting note: ${e.message}")
            }
        }
    }
    fun updateNote(id:Long, name: String, date: Long, dateEdited: String, text: String, reminder: Long, color: String, isHandwritten: Boolean,checkboxes: String) {
        viewModelScope.launch(Dispatchers.IO) {
            var n1= NotesDatabaseElement(id, name, date, dateEdited, text, reminder, color, isHandwritten, chekboxes = checkboxes)
            editNoteUseCase(n1)
            updateLinkedWidgets(context,n1)
        }
    }
    fun updateLinkedWidgets(context: Context, note: NotesDatabaseElement) {
        val manager = AppWidgetManager.getInstance(context)
        val ids = manager.getAppWidgetIds(ComponentName(context, MyNoteWidgetProvider::class.java))

        ids.forEach { id ->
            if (sharedPreferences.getLong("note_id_$id", -1) == note.id) {
                MyNoteWidgetProvider.updateAppWidget(
                    context, manager, id, note.name, note.text, note.id
                )
            }
        }
    }
    fun InsertTag(NoteId:Long,name: String, color: String, isActived: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            var id=InsertTagUseCase(name,color,isActived)
            InsertCurrentNoteTag(NoteId,id)
        }
    }

    fun GettAllTags(){
        viewModelScope.launch(Dispatchers.IO) {
            GetAllTagsUseCase().collect { tagsList ->
                _Tags.value = tagsList
            }
        }
    }
    fun InsertCurrentNoteTag(noteId: Long, tagId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("NotesListViewModel", "tag inserted in the note: ${noteId},${tagId}")
            InsertCurrentNoteTagUseCase(noteId, tagId)
            updateActiveTags(noteId)
        }
    }

    fun DeleteCurrentNoteTag(noteId: Long, tagId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("NotesListViewModel", "tag deleting from note: ${noteId},${tagId}")
            DeleteCurrentNoteTagUseCase(noteId, tagId)
            updateActiveTags(noteId)
        }
    }

    fun updateActiveTags(noteId: Long) {
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
                Log.d("NotesListViewModel", "тег из Заметки удаляется: ${updatedTags.toString()}")
            }.collect({})
        }
    }

    fun InsertNewPhoto(id: Long, noteId: Long, imageUrl: String) {
        viewModelScope.launch(Dispatchers.IO) {
            insertLinkedImageUseCase(id, noteId, imageUrl)
        }
    }

    fun DeletePhoto(id: Long, noteId: Long, imageUrl: String) {
        viewModelScope.launch(Dispatchers.IO) {
            DeleteLinkedImageUseCase(id, noteId, imageUrl)
        }
    }
    fun GetNotePhotos(noteId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            GetLinkedImagesUseCase(noteId).collect { photoList ->
                _images.value = photoList
            }
        }
    }

}
