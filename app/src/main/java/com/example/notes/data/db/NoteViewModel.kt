package com.example.notes.data.db

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.notes.data.db.entities.NoteEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class NoteViewModel(application: Application):AndroidViewModel(application) {
    private val repository:NotesRepository

    val allnotes : LiveData<List<NoteEntity>>

    init {
        val dao = NoteDatabase.getDatabase(application).getNoteDao()
        repository = NotesRepository(dao)
        allnotes = repository.allNotes
    }

    fun deleteNote(note : NoteEntity) = viewModelScope.launch ( Dispatchers.IO ){
        repository.delete(note)
    }

    fun insertOrUpdateNote(note : NoteEntity) = viewModelScope.launch ( Dispatchers.IO ){
        repository.insertOrUpdate(note)
    }
}