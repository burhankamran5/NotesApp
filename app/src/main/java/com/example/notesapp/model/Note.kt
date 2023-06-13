package com.example.notesapp.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity("Note")
@Parcelize
data class Note(
    val title: String,
    val description: String
) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
