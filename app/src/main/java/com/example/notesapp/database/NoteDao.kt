package com.example.notesapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.notesapp.model.Note
import kotlinx.coroutines.flow.Flow


@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM Note")
    fun getAllNotes(): LiveData<List<Note>>

    @Update
    suspend fun updateNote(note: Note)

    @Query("DELETE FROM Note")
    suspend fun deleteAllNotes()

    @Query("SELECT * FROM Note WHERE title = :query")
     fun searchNote(query: String): Flow<List<Note>>
}
