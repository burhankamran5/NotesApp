package com.example.notesapp

import com.example.notesapp.model.Note

interface ClickListener {
    fun onDeleteIcon(note: Note)
    fun onEditIcon(note: Note)
    fun onNoteClick(note: Note)

}
