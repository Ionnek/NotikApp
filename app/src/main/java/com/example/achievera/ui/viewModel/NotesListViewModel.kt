package com.example.achievera.ui.viewModel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.achievera.data.model.NotesDatabaseElement
import com.example.achievera.data.model.Tag
import com.example.achievera.domain.usecases.notesUs.DeleteNoteUseCase
import com.example.achievera.domain.usecases.notesUs.GetAllNotesUseCase
import com.example.achievera.domain.usecases.notesUs.InsertNoteUseCase
import com.example.achievera.domain.usecases.notesUs.EditNoteUseCase
import com.example.achievera.domain.usecases.notesUs.GetSearchQueryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest

import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesListViewModel @Inject constructor(
    private val getAllNotesUseCase: GetAllNotesUseCase,
    private val insertNoteUseCase: InsertNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val editNoteUseCase: EditNoteUseCase,
    private val searchQueryUseCase: GetSearchQueryUseCase
) : ViewModel(),INotesListViewModel {
    init {
        loadNotes()
    }
    private val _tags = mutableStateListOf<Tag>(
        Tag(0, "Dota 2","Red",false),
        Tag(1, "CS:GO","Blue",false),
        Tag(2, "Minecraft","Green",false),
        Tag(3, "League of Legends","Yellow",false),
        Tag(4, "Fortnite","Purple",false),
        Tag(5, "Overwatch","Orange",false),
        Tag(6, "Valorant","Pink",false),
        Tag(7, "Hearthstone","Brown",false),
        Tag(8, "World of Warcraft","Gray",false),
        Tag(9, "Rainbow Six Siege","Cyan",false),
        Tag(10, "Rocket League","Indigo",false)
    )

    val tags: List<Tag> = _tags

    private val _notesFor = MutableLiveData<List<NotesDatabaseElement>>()
    override val notesFor: LiveData<List<NotesDatabaseElement>> get() = _notesFor

    private val _note = MutableLiveData<NotesDatabaseElement>()
    override val note: LiveData<NotesDatabaseElement> get() = _note

    private val currentQuery = MutableStateFlow("")

    fun ToggleTag(tag: Tag) {
        val currentTag: Tag = tag
        viewModelScope.launch {
            val index =tags.indexOfFirst {it.id == tag.id}
            if (index != -1){
                val currentTag: Tag = tags[index]
                val updatedTag: Tag=currentTag.copy(isActived = !currentTag.isActived)
                _tags[index] = updatedTag
                if (updatedTag.isActived) {
                    // Перемещаем активный тег в начало списка
                    _tags.removeAt(index)
                    _tags.add(0, updatedTag)
                } else {
                    // Если тег деактивируется, можно решить, куда его переместить
                    // В данном примере мы оставляем его на текущей позиции
                }
        }}
        }
    override fun setQuery(query: String) {
        currentQuery.value = query
    }

    override val notes: Flow<PagingData<NotesDatabaseElement>> =
        currentQuery
            .flatMapLatest { query ->
                searchQueryUseCase(query) // Передаём строку запроса
            }
            .cachedIn(viewModelScope)

    override fun loadNotes() {
        viewModelScope.launch {
            if(getAllNotesUseCase()!=null){
                _notesFor.value = getAllNotesUseCase()}else{
                Log.d("NotesListViewModel", "Список пуст")
            }
        }
    }
    override fun getNoteById(id: Long) {
            loadNotes()
        viewModelScope.launch {
            val list: List<NotesDatabaseElement> = _notesFor.value?: emptyList()
            for(listElement in list){
            if(listElement.id==id){
                Log.d("NotesListViewModel", "Заметка найдена: ${listElement.id},${listElement.text}, вьюмодель ${this@NotesListViewModel}")
                _note.value= listElement
            }
            }
            Log.d("NotesListViewModel", "Заметка загружена в лайф дата: ${note.value?.id},${note.value?.text}")
        }
    }

    override fun insertNote(name: String, date: Long, dateEdited: String, text: String, reminder: Long, color: String, isHandwritten: Boolean) {
        viewModelScope.launch {
            var n1= NotesDatabaseElement(0, name, date, dateEdited, text, reminder, color, isHandwritten)
            insertNoteUseCase(n1)
            loadNotes()
            setQuery("")

        }
    }
    override fun deleteNote(note: NotesDatabaseElement) {
        viewModelScope.launch {
            try {
                val currentNotes = _notesFor.value
                if (currentNotes != null) {
                    if (currentNotes.contains(note)) {
                        deleteNoteUseCase(note.id)
                        loadNotes()
                        setQuery("")
                        Log.d("NotesListViewModel", "Заметка удалена: ${note.id}")
                    } else {
                        Log.w("NotesListViewModel", "Попытка удалить несуществующую заметку: ${note.id}")
                    }
                }
            } catch (e: Exception) {
                Log.e("NotesListViewModel", "Ошибка при удалении заметки: ${e.message}")
            }
        }
    }
    override fun updateNote(id:Long, name: String, date: Long, dateEdited: String, text: String, reminder: Long, color: String, isHandwritten: Boolean) {
        viewModelScope.launch {
            var n1= NotesDatabaseElement(id, name, date, dateEdited, text, reminder, color, isHandwritten)
            editNoteUseCase(n1)
            loadNotes()
            setQuery("")
            }
    }
}