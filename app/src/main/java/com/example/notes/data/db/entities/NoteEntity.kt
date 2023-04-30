package com.example.notes.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long?,
    val title: String = "",
    val note: String = "",
    val date : String = ""
) : java.io.Serializable