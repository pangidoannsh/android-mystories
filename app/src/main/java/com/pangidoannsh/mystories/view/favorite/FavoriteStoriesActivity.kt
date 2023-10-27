package com.pangidoannsh.mystories.view.favorite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.pangidoannsh.mystories.databinding.ActivityFavoriteStoriesBinding
import com.pangidoannsh.mystories.view.ViewModelFactory
import com.pangidoannsh.mystories.view.story.StoryAdapter

class FavoriteStoriesActivity : AppCompatActivity() {
    private var _binding: ActivityFavoriteStoriesBinding? = null
    private val binding get() = _binding

    private val viewModel by viewModels<FavoriteStoriesViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFavoriteStoriesBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.let {
            setSupportActionBar(it.toolbar)
            it.toolbar.setNavigationOnClickListener {
                finish()
            }
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupObserver()
    }

    private fun setupObserver() {
        viewModel.getFavoriteStories().observe(this) { stories ->
            setupListLayout()
            binding?.let {
                it.rvStories.adapter = StoryAdapter(stories)
                it.notFound.visibility = if (stories.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }

    private fun setupListLayout() {
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding?.let { bind ->
            bind.rvStories.layoutManager = layoutManager
            bind.rvStories.setHasFixedSize(true)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}