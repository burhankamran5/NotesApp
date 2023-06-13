package com.example.notesapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.ClickListener
import com.example.notesapp.databinding.NoteDesignBinding
import com.example.notesapp.model.Note
import java.lang.ref.WeakReference


class NotesAdapter(
    var note: List<Note>, private val listener: WeakReference<ClickListener>,

    ) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {


    class NoteViewHolder(
        private val binding: NoteDesignBinding,
        private val listener: WeakReference<ClickListener>
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(note: Note) {
            binding.title.text = note.title
            binding.description.text = note.description
            binding.del.setOnClickListener {
                listener.get()?.onDeleteIcon(note)
            }
            binding.edit.setOnClickListener {
                listener.get()?.onEditIcon(note)
            }
            binding.root.setOnClickListener {
                listener.get()?.onNoteClick(note)
            }

        }


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding =
            NoteDesignBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding, listener)
    }


    override fun getItemCount(): Int {
        return note.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterList(notelist: List<Note>) {
        note = notelist
        notifyDataSetChanged()
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(note[position])
    }


}
