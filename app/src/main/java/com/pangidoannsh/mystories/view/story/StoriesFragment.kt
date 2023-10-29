package com.pangidoannsh.mystories.view.story

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.pangidoannsh.mystories.adapter.LoadingStateAdapter
import com.pangidoannsh.mystories.adapter.StoryAdapter
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
        setupObserver()
        setupListLayout()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun updateListStories() {
//        viewModel.getListStories()
    }

    private fun setupObserver() {
        val adapter = StoryAdapter()
        binding?.rvStories?.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        setupListLayout()

        viewModel.stories.observe(viewLifecycleOwner) {
            adapter.submitData(lifecycle, it)
        }
        binding?.let { bind ->
            adapter.addLoadStateListener { loadStates ->
                when (loadStates.source.refresh) {
                    is LoadState.Loading -> {
                        bind.loadingBar.visibility = View.VISIBLE
                    }

                    is LoadState.NotLoading -> {
                        bind.loadingBar.visibility = View.GONE
                    }

                    else -> {}
                }
            }
        }
//        viewModel.isLoading.observe(viewLifecycleOwner) {
//            binding?.loadingBar?.visibility = if (it) View.VISIBLE else View.GONE
//        }
    }

    private fun setupListLayout() {
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding?.let { bind ->
            bind.rvStories.layoutManager = layoutManager
            bind.rvStories.setHasFixedSize(true)
        }
    }
}