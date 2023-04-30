package com.example.notes.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.notes.data.db.entities.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(note: NoteEntity)

    @Delete
    fun delete(note: NoteEntity)

    /*
        @Query("Select * From NoteEntity Order By priority Desc")
        fun selectAll(): Flow<List<NoteEntity>>
        */
    @Query("Select * From NoteEntity Order by date Desc")
    fun getAllNotes() : LiveData<List<NoteEntity>>

}