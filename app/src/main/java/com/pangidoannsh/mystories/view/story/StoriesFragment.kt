package com.pangidoannsh.mystories.view.story

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.pangidoannsh.mystories.databinding.FragmentStoriesBinding
import com.pangidoannsh.mystories.view.ViewModelFactory

class StoriesFragment : Fragment() {
    private var _binding: FragmentStoriesBinding? = null
    private val binding get() = _binding

    private val viewModel by activityViewModels<StoriesViewModel> {
        ViewModelFactory.getInstance(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStoriesBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getListStories()
        setupObserver()
        setupListLayout()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun updateListStories() {
        viewModel.getListStories()
    }

    private fun setupObserver() {
        viewModel.listStories.observe(viewLifecycleOwner) { listStories ->
            setupListLayout()
            binding?.rvStories?.adapter = StoryAdapter(listStories)
        }
        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding?.loadingBar?.visibility = if (it) View.VISIBLE else View.GONE
        }

    }

    private fun setupListLayout() {
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding?.let { bind ->
            bind.rvStories.layoutManager = layoutManager
            bind.rvStories.setHasFixedSize(true)
        }
    }
}