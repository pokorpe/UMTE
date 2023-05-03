package com.example.notes.data.db

import androidx.lifecycle.LiveData
import com.example.notes.data.db.dao.NoteDao
import com.example.notes.data.db.entities.NoteEntity

class NotesRepository (private val noteDao: NoteDao){

    val allNotes : LiveData<List<NoteEntity>> = noteDao.getAllNotes()
    fun insertOrUpdate(note:NoteEntity) {
        noteDao.insertOrUpdate(note)
    }

    fun delete(note:NoteEntity) {
        noteDao.delete(note)
    }

}