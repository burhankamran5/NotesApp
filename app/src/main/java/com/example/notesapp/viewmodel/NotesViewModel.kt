package com.example.notesapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapp.model.Note
import com.example.notesapp.repo.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class NotesViewModel(
    private val notesRepository: NoteRepository
) : ViewModel() {

    val result = MutableStateFlow<List<Note>>(emptyList())
    val searchNote = MutableStateFlow("")

    fun search() {
        viewModelScope.launch {
            searchNote.debounce(400L).flatMapLatest { keyword ->
                notesRepository.searchNote(keyword)
            }.collect {
                result.value = it
            }
        }
    }

    suspend fun fetchAllNotes() = notesRepository.getAllNotes()

    suspend fun insertNote(note: Note) = viewModelScope.launch {
        notesRepository.insertNote(note)
    }

    suspend fun deleteNote(note: Note) = viewModelScope.launch {
        notesRepository.deleteNote(note)
    }

    suspend fun updateNote(note: Note) = viewModelScope.launch {
        notesRepository.updateNote(note)
    }

    suspend fun deleteAllNotes() = notesRepository.deleteAllNotes()


}