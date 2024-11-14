package com.example.achievera.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.achievera.data.model.NotesDatabaseElement
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

// Мок-ViewModel для превью
class MockNotesListViewModel : ViewModel(), INotesListViewModel {

    // Predefined data for the list of notes
    private val _notesFor = MutableLiveData<List<NotesDatabaseElement>>(
        listOf(
            NotesDatabaseElement(
                id = 1L,
                name = "Mock Note 1",
                text = "This is the first mock note.",
                date = 123456789L,
                dateEdited = "01.01.2024",
                reminder = 0L,
                color = "red",
                isHandwritten = false
            ),
            NotesDatabaseElement(
                id = 2L,
                name = "Mock Note 2",
                text = "This is the second mock note.",
                date = 987654321L,
                dateEdited = "02.01.2024",
                reminder = 0L,
                color = "blue",
                isHandwritten = true
            )
        )
    )
    override val notesFor: LiveData<List<NotesDatabaseElement>> get() = _notesFor

    // Predefined data for a single note
    private val _note = MutableLiveData<NotesDatabaseElement>()
    override val note: LiveData<NotesDatabaseElement> get() = _note

    // StateFlow for managing search queries
    private val currentQuery = MutableStateFlow("")

    // Implement the Flow<PagingData<NotesDatabaseElement>>
    override val notes: Flow<PagingData<NotesDatabaseElement>> =
        currentQuery.flatMapLatest { query ->
            val filteredNotes = if (query.isEmpty()) {
                _notesFor.value ?: emptyList()
            } else {
                _notesFor.value?.filter {
                    it.name.contains(query, ignoreCase = true) || it.text.contains(query, ignoreCase = true)
                } ?: emptyList()
            }
            flow { emit(PagingData.from(filteredNotes)) }
        }

    override fun setQuery(query: String) {
        currentQuery.value = query
    }

    override fun loadNotes() {
        // In the mock, notes are already loaded in _notesFor initialization
    }

    override fun getNoteById(id: Long) {
        val foundNote = _notesFor.value?.find { it.id == id }
    }

    override fun insertNote(
        name: String,
        date: Long,
        dateEdited: String,
        text: String,
        reminder: Long,
        color: String,
        isHandwritten: Boolean
    ) {
        val newId = (_notesFor.value?.maxOfOrNull { it.id } ?: 0L) + 1
        val newNote = NotesDatabaseElement(
            id = newId,
            name = name,
            text = text,
            date = date,
            dateEdited = dateEdited,
            reminder = reminder,
            color = color,
            isHandwritten = isHandwritten
        )
        val updatedNotes = _notesFor.value?.toMutableList() ?: mutableListOf()
        updatedNotes.add(newNote)
        _notesFor.value = updatedNotes
    }

    override fun deleteNote(note: NotesDatabaseElement) {
        val updatedNotes = _notesFor.value?.toMutableList()
        updatedNotes?.remove(note)
        _notesFor.value = updatedNotes ?: emptyList()
    }

    override fun updateNote(
        id: Long,
        name: String,
        date: Long,
        dateEdited: String,
        text: String,
        reminder: Long,
        color: String,
        isHandwritten: Boolean
    ) {
        val updatedNotes = _notesFor.value?.map {
            if (it.id == id) {
                it.copy(
                    name = name,
                    date = date,
                    dateEdited = dateEdited,
                    text = text,
                    reminder = reminder,
                    color = color,
                    isHandwritten = isHandwritten
                )
            } else {
                it
            }
        }
        _notesFor.value = updatedNotes ?: emptyList()
    }
}
