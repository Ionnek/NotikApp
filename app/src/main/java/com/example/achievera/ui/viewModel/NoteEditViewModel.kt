package com.example.achievera.ui.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.achievera.R
import com.example.achievera.data.model.LinkedImage
import com.example.achievera.data.model.NotesDatabaseElement
import com.example.achievera.data.model.Tag
import com.example.achievera.domain.usecases.linkedImagesUs.DeleteLinkedImageUseCase
import com.example.achievera.domain.usecases.linkedImagesUs.DeleteLinkedImagesByNoteIdUseCase
import com.example.achievera.domain.usecases.linkedImagesUs.GetLinkedImagesUseCase
import com.example.achievera.domain.usecases.linkedImagesUs.InsertLinkedImageUseCase
import com.example.achievera.domain.usecases.notesUs.DeleteNoteUseCase
import com.example.achievera.domain.usecases.notesUs.EditNoteUseCase
import com.example.achievera.domain.usecases.notesUs.GetNoteByIdUseCase
import com.example.achievera.domain.usecases.notesUs.GetSearchQueryUseCase
import com.example.achievera.domain.usecases.notesUs.InsertNoteUseCase
import com.example.achievera.domain.usecases.tagsUs.DeleteCurrentNoteTagUseCase
import com.example.achievera.domain.usecases.tagsUs.DeleteTagUseCase
import com.example.achievera.domain.usecases.tagsUs.GetAllTagsUseCase
import com.example.achievera.domain.usecases.tagsUs.GetCurrentNoteTagsUseCase
import com.example.achievera.domain.usecases.tagsUs.GetNotesByTagUseCase
import com.example.achievera.domain.usecases.tagsUs.InsertCurrentNoteTagUseCase
import com.example.achievera.domain.usecases.tagsUs.InsertTagUseCase
import com.example.achievera.ui.view.NoteEdit.LineWithCheckBoxAndCursorDetect
import com.example.achievera.ui.view.NoteEdit.deserialize
import com.example.achievera.ui.view.NoteEdit.serialize
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
class NoteEditViewModel @Inject constructor(
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val editNoteUseCase: EditNoteUseCase,
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
    private val GetNoteByIdUseCase: GetNoteByIdUseCase,
    private val GetNotesByTagUseCase: GetNotesByTagUseCase
) : ViewModel(){
    init {
        GettAllTags()
    }
    var selectedLine by mutableIntStateOf(0)
        private set
    var isImageActive by mutableStateOf(false)
        private set
    var isChekboxesActive by mutableStateOf(false)
        private set
    var linesList by mutableStateOf<List<LineWithCheckBoxAndCursorDetect>>(emptyList())
        private set
    var NNoteId by mutableStateOf<Long>(0)
        private set
    fun setNoteId(id:Long){
        NNoteId=id
    }
    fun assignLinesList(text: String){
        linesList=deserialize(text)
    }

    fun assignReadyLinesList(text: List<LineWithCheckBoxAndCursorDetect>){
        linesList=text
    }
    fun updateSelectedLine(LineId:Int){
        selectedLine=LineId
    }
    fun linesListEmtyChek():Boolean{
        if(linesList.isNotEmpty()){
        for (line in linesList){
            if (line.text.text!=""){
                return false
            }else{
                return true}
        }}
        return true

    }
    fun changeLineChekboxStatus(){
        linesList = linesList.map { line ->
            if (line.id == selectedLine) {
                // Создаем копию линии с инвертированным значением isChecked
                line.copy(hasCheckbox = !line.hasCheckbox, isChecked = false)
            } else {
                line
            }
        }
    }
    fun addEmptyLine(hasChebox: Boolean =false) {
        // Здесь можно задать id, например, как максимальное значение id + 1
        linesList = linesList + LineWithCheckBoxAndCursorDetect(
            id = linesList.size + 1,
            text = TextFieldValue(""),
            isChecked = false,
            hasCheckbox = hasChebox
        )
    }
    fun addEmptyInsideLine(hasChebox: Boolean = false):Int {
        // Создаём новую линию с уникальным id (можно улучшить логику генерации id, если потребуется)
        val newLine = LineWithCheckBoxAndCursorDetect(
            id = linesList.size + 1,
            text = TextFieldValue(""),
            isChecked = false,
            hasCheckbox = hasChebox
        )

        // Определяем индекс для вставки:
        // Если selectedLine указывает на существующий элемент, вставляем после него,
        // иначе (например, если selectedLine выходит за пределы списка) — добавляем в конец.
        val insertIndex =
            if (selectedLine in linesList.indices) selectedLine + 1 else linesList.size

        // Если linesList является неизменяемым списком, то преобразуем его в изменяемый,
        // вставляем новый элемент, затем присваиваем обратно.
        val mutableList = linesList.toMutableList()
        mutableList.add(insertIndex, newLine)
        linesList = mutableList.toList()
        return insertIndex
    }
    fun removeLine(lineid:Int){
        linesList = linesList.filter { it.id != lineid }
    }
    // Метод для переключения состояния
    fun toggleImageActive(state:Boolean) {
        isImageActive = state
    }
    fun toggleChekBoxesActive(state:Boolean) {
        isChekboxesActive = state
    }

    private val _images = MutableStateFlow<List<LinkedImage>>(emptyList())
    val images: StateFlow<List<LinkedImage>> get() = _images

    private val _Tags = MutableStateFlow<List<Tag>>(emptyList())
    val tags: StateFlow<List<Tag>> get() = _Tags

    private val _FilterTags = MutableStateFlow<List<Tag>>(emptyList())
    val FilterTags: StateFlow<List<Tag>> get() = _FilterTags

    private val _NoteTags = MutableStateFlow<List<Tag>>(emptyList())
    val NoteTags: StateFlow<List<Tag>> get() = _NoteTags

    private val _note = MutableLiveData<NotesDatabaseElement>()
    val note: LiveData<NotesDatabaseElement> get() = _note


    private val _insertedNoteId = MutableStateFlow<Long>(0)

    private val _eventFlow = MutableSharedFlow<NotesListEvent>(replay = 0)
    val eventFlow = _eventFlow.asSharedFlow()

    val testImageList = listOf(
        R.drawable.test_photo_1,
        R.drawable.test_photo_2,
        R.drawable.test_photo_3,
        R.drawable.test_photo_4,
        R.drawable.test_photo_1,
        R.drawable.test_photo_2,
        R.drawable.test_photo_3
    )

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
                Log.e("NotesListViewModel", "Ошибка при удалении заметки: ${e.message}")
            }
        }
    }
    fun updateNote(id:Long, name: String, date: Long, dateEdited: String, text: String, reminder: Long, color: String, isHandwritten: Boolean,checkboxes: String) {
        viewModelScope.launch(Dispatchers.IO) {
            var n1= NotesDatabaseElement(id, name, date, dateEdited, text, reminder, color, isHandwritten, chekboxes = checkboxes)
            editNoteUseCase(n1)
        }
    }
    fun InsertTag(NoteId:Long,name: String, color: String, isActived: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            var id=InsertTagUseCase(name,color,isActived)
            InsertCurrentNoteTag(NoteId,id)
        }
    }
    fun DeleteTag(tag: Tag) {
        viewModelScope.launch(Dispatchers.IO) {
            DeleteTagUseCase(tag)
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
            Log.d("NotesListViewModel", "тег в Заметку вставляется: ${noteId},${tagId}")
            InsertCurrentNoteTagUseCase(noteId, tagId)
            updateActiveTags(noteId)
        }
    }

    fun DeleteCurrentNoteTag(noteId: Long, tagId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("NotesListViewModel", "тег из Заметки удаляется: ${noteId},${tagId}")
            DeleteCurrentNoteTagUseCase(noteId, tagId)
            updateActiveTags(noteId)
        }
    }

    fun updateActiveTags(noteId: Long) {//получить список всех тегов но с подсвеченными активными по этой заметке)
        viewModelScope.launch(Dispatchers.IO) {
            // Получаем все теги и теги, привязанные к заметке, с помощью combine
            combine(
                GetAllTagsUseCase(), // Все теги
                GetCurrentNoteTagsUseCase(noteId) // Теги для конкретной заметки
            ) { allTags, noteTags ->
                // Создаем новый список активных тегов
                val updatedTags = allTags.map { tag ->
                    // Если тег привязан к заметке, делаем его активным
                    if (noteTags.contains(tag)) {
                        tag.copy(isActived = true)
                    } else {
                        tag.copy(isActived = false)
                    }
                }
                // Обновляем список активных тегов
                _NoteTags.value = updatedTags
                Log.d("NotesListViewModel", "тег из Заметки удаляется: ${updatedTags.toString()}")
            }.collect({}) // Сбор данных и выполнение логики
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

    fun DeleteAllPhotos(noteId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            DeleteLinkedImagesByNoteIdUseCase(noteId)
        }
    }
    fun LinesToGoodTexts():String{
        var TextStingBuilder=""
        for (line in linesList){
            if(line.hasCheckbox){
                if(line.isChecked){
                    TextStingBuilder=TextStingBuilder+"☑"
                }else{TextStingBuilder=TextStingBuilder+"☐"}
            }
            TextStingBuilder=TextStingBuilder+line.text.text
            TextStingBuilder=TextStingBuilder+"\n"
        }
        return TextStingBuilder
    }
    //штуки в кастомную обертку
    //штуки в кастомную обертку
    //штуки в кастомную обертку
    //штуки в кастомную обертку
}
