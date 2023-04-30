package com.example.notes.data.db

import androidx.lifecycle.LiveData
import com.example.notes.data.db.NoteDatabase
import com.example.notes.data.db.dao.NoteDao
import com.example.notes.data.db.entities.NoteEntity
import java.util.concurrent.Flow

class NotesRepository (private val noteDao: NoteDao){

    val allNotes : LiveData<List<NoteEntity>> = noteDao.getAllNotes()
    fun insertOrUpdate(note:NoteEntity) {
        noteDao.insertOrUpdate(note)
    }

    fun delete(note:NoteEntity) {
        noteDao.delete(note)
    }

}