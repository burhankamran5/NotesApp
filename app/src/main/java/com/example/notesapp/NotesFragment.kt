package com.example.notesapp

import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.notesapp.database.NoteDatabase
import com.example.notesapp.databinding.FragmentNotesBinding
import com.example.notesapp.model.Note
import com.example.notesapp.repo.NoteRepository
import com.example.notesapp.viewmodel.NotesViewModel
import com.example.notesapp.viewmodel.NotesViewModelProvideFactory
import kotlinx.coroutines.launch


class NotesFragment : Fragment() {
    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!
    private lateinit var notesViewModel: NotesViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val repository = NoteRepository(NoteDatabase.getDataBase(requireContext()))
        notesViewModel = ViewModelProvider(
            this, NotesViewModelProvideFactory(repository)
        ).get(NotesViewModel::class.java)

        val note: Note? = arguments?.getParcelable("Note")
        note.let {
            binding.titleInput.setText(note?.title.orEmpty())
            binding.notesInput.setText(note?.description.orEmpty())
        }

        binding.saveBtn.setOnClickListener {
            lifecycleScope.launch {
                val titleText = binding.titleInput.text.toString()
                val descriptionText = binding.notesInput.text.toString()
                if (!TextUtils.isEmpty(titleText) || !TextUtils.isEmpty(descriptionText)) {
                    val updatedNote = Note(titleText, descriptionText)
                    note?.let {
                        updatedNote.id = it.id
                    }
                    notesViewModel.insertNote(updatedNote)
                    Log.d("RoomTitle:", "${titleText}")
                    Log.d("RoomDesc:", "${descriptionText}")
                    val action = NotesFragmentDirections.actionNotesFragmentToBaseFragment()
                    findNavController().navigate(action)
                }
            }
        }

    }


}
