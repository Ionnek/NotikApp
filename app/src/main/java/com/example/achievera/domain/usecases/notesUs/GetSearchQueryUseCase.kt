package com.example.achievera.domain.usecases.notesUs

import android.util.Log
import androidx.paging.PagingData
import com.example.achievera.data.model.NotesDatabaseElement
import com.example.achievera.data.model.Tag
import com.example.achievera.domain.repositoryInterfaces.INotesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSearchQueryUseCase @Inject constructor(private val notesRepository: INotesRepository) {
    operator fun invoke(query: String,Ids:Set<Long>): Flow<PagingData<NotesDatabaseElement>> {
        Log.d("NotesListViewModel", "Заметки найдеры: ${notesRepository.getSearchQuery(query,Ids)}")
        return notesRepository.getSearchQuery(query,Ids)
    }
}