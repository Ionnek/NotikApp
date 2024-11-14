package com.example.achievera.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.example.achievera.data.model.NotesDatabaseElement
import kotlinx.coroutines.flow.Flow

interface INotesListViewModel {
    val notes: Flow<PagingData<NotesDatabaseElement>>
    val notesFor: LiveData<List<NotesDatabaseElement>>
    val note: LiveData<NotesDatabaseElement>

    fun setQuery(query: String)
    fun loadNotes()
    fun getNoteById(id: Long)
    fun insertNote(
        name: String,
        date: Long,
        dateEdited: String,
        text: String,
        reminder: Long,
        color: String,
        isHandwritten: Boolean
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
        isHandwritten: Boolean
    )
}