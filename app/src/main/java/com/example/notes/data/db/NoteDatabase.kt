package com.example.notes.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.notes.data.db.dao.NoteDao
import com.example.notes.data.db.entities.NoteEntity

@Database(version = NoteDatabase.Version, entities = [NoteEntity::class,],)
abstract class NoteDatabase : RoomDatabase(){
    abstract fun noteDao() : NoteDao

    companion object {
        const val Version = 1
        const val Name = "NotesDatabase"
    }
}