package com.example.achievera.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.achievera.data.model.NotesDatabaseElement
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.example.achievera.R
import com.example.achievera.data.model.LinkedImage
import com.example.achievera.data.model.Tag
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class MockNotesListViewModel : ViewModel(), INotesListViewModel {
    override var MenoState=1
    private val _notesFor = MutableLiveData(
        listOf(
            NotesDatabaseElement(
                id = 1L,
                name = "Mock Note 1",
                text = "This is the first mock note.",
                date = 123456789L,
                dateEdited = "01.01.2024",
                reminder = 0L,
                color = "red",
                isHandwritten = false,
                chekboxes= "{}"
            ),
            NotesDatabaseElement(
                id = 2L,
                name = "Mock Note 2",
                text = "This is the second mock note.",
                date = 987654321L,
                dateEdited = "02.01.2024",
                reminder = 0L,
                color = "blue",
                isHandwritten = true,
                chekboxes= "{}"
            )
        )
    )
    private val _images = MutableStateFlow(
        listOf(
            LinkedImage(
            id = 1L,
            imageUri = "",
            noteId = 1L
            )
        )
    )
    override val testImageList = listOf(
        R.drawable.test_photo_1,
        R.drawable.test_photo_2,
        R.drawable.test_photo_3,
        R.drawable.test_photo_4,
        R.drawable.test_photo_1,
        R.drawable.test_photo_2,
        R.drawable.test_photo_3
    )
    private val _Tags = MutableStateFlow(
        listOf(
            Tag(
                id = 1L,
                name = "Mock Tag 1",
                color = "Red",
                isActived = false
            ),
            Tag(
                id = 2L,
                name = "Mock Tag 2",
                color = "Blue",
                isActived = false
            ),
            Tag(
                id = 3L,
                name = "Mock Tag 3",
                color = "Purple",
                isActived = false
            ),
            Tag(
                id = 4L,
                name = "Mock Tag 4",
                color = "Green",
                isActived = false
            )

        )
    )
    override fun getNotesByTag(tagId: Long): Flow<List<NotesDatabaseElement>>{
        return emptyFlow()
    }
    override fun addTagToQuery(tagId: Long) {}

    private val _note = MutableLiveData<NotesDatabaseElement>()
    override val note: LiveData<NotesDatabaseElement> get() = _note
    override val tags: StateFlow<List<Tag>> get() = _Tags
    override val FilterTags: StateFlow<List<Tag>> = MutableStateFlow(emptyList())
    private val currentQuery = MutableStateFlow("")
    override val NoteTags: StateFlow<List<Tag>> = MutableStateFlow(emptyList())

    override val eventFlow: Flow<NotesListEvent> = flowOf()
    override val insertedNoteId: StateFlow<Long> = MutableStateFlow(0)
    override val images: StateFlow<List<LinkedImage>>
        get() = _images

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

    override fun updateTagsToActiveFilter() {}
    override fun removeTagFromQuery(tagId: Long) {}
    override fun setQuery(query: String) {}
    override fun InsertStandaloneTag(name: String, color: String, isActived: Boolean) {}
    override fun getNoteById(id: Long) {}

    override fun insertNote(
        name: String,
        date: Long,
        dateEdited: String,
        text: String,
        reminder: Long,
        color: String,
        isHandwritten: Boolean,
        chekboxes: String
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
            isHandwritten = isHandwritten,
            chekboxes= "{}"
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
        isHandwritten: Boolean,
        chekboxes:String
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
                    isHandwritten = isHandwritten,
                    chekboxes= "{}"
                )
            } else {
                it
            }
        }
        _notesFor.value = updatedNotes ?: emptyList()
    }

    override fun InsertTag(id: Long,s: String, s1: String, a: Boolean){}
    override fun DeleteTag(tag:Tag){}
    override fun GettAllTags(){}
    override fun InsertCurrentNoteTag(noteId: Long, tagId: Long) {}
    override fun DeleteCurrentNoteTag(noteId: Long, tagId: Long) {}
    override fun InsertNewPhoto(id: Long,noteId: Long,imageUrl:String){}
    override fun DeletePhoto(id: Long,noteId: Long,imageUrl:String){}
    override fun updateActiveTags(noteId: Long) {}
    override fun GetNotePhotos(noteId: Long) {}
    override fun DeleteAllPhotos(noteId: Long) {}
}
