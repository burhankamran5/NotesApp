package com.example.notesapp.repo

import com.example.notesapp.database.NoteDatabase
import com.example.notesapp.model.Note

class NoteRepository(private val db: NoteDatabase) {

    suspend fun insertNote(note: Note) = db.noteDao().insertNote(note)

    suspend fun deleteNote(note: Note) = db.noteDao().deleteNote(note)

    suspend fun updateNote(note: Note) = db.noteDao().updateNote(note)

    suspend fun deleteAllNotes() = db.noteDao().deleteAllNotes()

    suspend fun getAllNotes() = db.noteDao().getAllNotes()

     fun searchNote(query: String) = db.noteDao().searchNote(query)

}