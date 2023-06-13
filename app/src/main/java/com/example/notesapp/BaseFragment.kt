package com.example.notesapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.notesapp.adapter.NotesAdapter
import com.example.notesapp.database.NoteDatabase
import com.example.notesapp.databinding.FragmentBaseBinding
import com.example.notesapp.model.Note
import com.example.notesapp.repo.NoteRepository
import com.example.notesapp.viewmodel.NotesViewModel
import com.example.notesapp.viewmodel.NotesViewModelProvideFactory
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

class BaseFragment : Fragment(), ClickListener {

    private var _binding: FragmentBaseBinding? = null
    private val binding get() = _binding!!
    private lateinit var notesViewModel: NotesViewModel
    lateinit var noteList: MutableList<Note>
    lateinit var notesAdapter: NotesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBaseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val repository = NoteRepository(NoteDatabase.getDataBase(requireContext()))
        notesViewModel = ViewModelProvider(
            this,
            NotesViewModelProvideFactory(repository)
        )[NotesViewModel::class.java]
        binding.apply {

            noteList = mutableListOf()
            searchbar.setOnQueryTextListener(object :
                SearchView.OnQueryTextListener,
                android.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    filter(newText.toString())
                    return false
                }

            }
            )


            viewLifecycleOwner.lifecycleScope.launch {
                notesViewModel.fetchAllNotes().observe(viewLifecycleOwner, Observer {
                    noteList = it.toMutableList()
                    notesAdapter = NotesAdapter(noteList, WeakReference(this@BaseFragment))
                    binding.notesRV.layoutManager = GridLayoutManager(requireContext(), 2)
                    binding.notesRV.adapter = notesAdapter
                })
            }
            nextIcon.setOnClickListener {
                val action = BaseFragmentDirections.actionBaseFragmentToNotesFragment()
                findNavController().navigate(action)
            }

        }


    }


    fun filter(text: String) {
        if (::notesAdapter.isInitialized) {
            val filteredlist: MutableList<Note> = mutableListOf()
            for (item in noteList) {
                if (item.title.toLowerCase().contains(text.toLowerCase())) {
                    filteredlist.add(item)
                }
            }
            if (filteredlist.isEmpty()) {
                Toast.makeText(context, "No Notes Founded", Toast.LENGTH_SHORT).show()
            } else {
                notesAdapter.filterList(filteredlist)
            }
        }
    }


    override fun onDeleteIcon(note: Note) {
        lifecycleScope.launch {
            notesViewModel.deleteNote(note)
        }
    }

    override fun onEditIcon(note: Note) {
        val bundle = Bundle()
        bundle.putParcelable("Note", note)
        findNavController().navigate(R.id.action_baseFragment_to_notesFragment, bundle)
        Log.d("Bundle:", "$bundle")

    }

    override fun onNoteClick(note: Note) {
        val bundle = Bundle()
        bundle.putParcelable("Note", note)
        findNavController().navigate(R.id.action_baseFragment_to_notesFragment, bundle)
        Log.d("Bundle:", "$bundle")
    }
}

