package com.pangidoannsh.mystories.view.story.detailstory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pangidoannsh.mystories.R
import com.pangidoannsh.mystories.databinding.ActivityDetailStoryBinding
import com.bumptech.glide.request.target.Target
import com.pangidoannsh.mystories.data.api.response.StoryResponse
import com.pangidoannsh.mystories.data.local.entity.FavoriteStories
import com.pangidoannsh.mystories.mapStoryToFavorite
import com.pangidoannsh.mystories.view.ViewModelFactory

class DetailStoryActivity : AppCompatActivity() {
    private var _binding: ActivityDetailStoryBinding? = null
    private val binding get() = _binding

    private val viewModel by viewModels<DetailStoryViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setSupportActionBar(binding?.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(getDrawable(R.drawable.ic_back_arrow));

        setupComponents()
        setupObserver()
    }

    private fun setupObserver() {
        viewModel.getFavoriteStory()?.observe(this) { story ->
            val isFavorite = story !== null
            setIconBtnFavorite(isFavorite)
            btnFavoriteAction(isFavorite)
        }
    }

    private fun setIconBtnFavorite(isFavorite: Boolean) {
        binding?.let { bind ->
            if (isFavorite) {
                bind.btnFavorite.setImageDrawable(
                    ContextCompat.getDrawable(
                        bind.btnFavorite.context,
                        R.drawable.ic_favorite_full
                    )
                )
            } else {
                bind.btnFavorite.setImageDrawable(
                    ContextCompat.getDrawable(
                        bind.btnFavorite.context,
                        R.drawable.ic_favorite_rounded
                    )
                )
            }
        }
    }

    private fun btnFavoriteAction(isFavorite: Boolean) {
        binding?.let { bind ->
            bind.btnFavorite.setOnClickListener {
                if (isFavorite) viewModel.removeFavorite()
                else viewModel.setFavorite()
            }
        }
    }

    private fun setupComponents() {
        val story = getExtra()

        viewModel.initStoryData(story)

        binding?.let { bind ->
            bind.storyCreator.text = story.name
            bind.description.text = story.description

            if (story.photoUrl !== null) {
                Glide.with(this)
                    .load(story.photoUrl)
                    .apply(
                        RequestOptions()
                            .override(
                                Target.SIZE_ORIGINAL,
                                resources.getDimensionPixelSize(R.dimen.full_height_image)
                            )
                            .fitCenter()
                    )
                    .into(bind.imageStory)
            }
        }


    }

    private fun getExtra(): FavoriteStories {
        val id = intent.getStringExtra(EXTRA_ID)
        val name = intent.getStringExtra(EXTRA_NAME)
        val photoUrl = intent.getStringExtra(EXTRA_PHOTO_URL)
        val description = intent.getStringExtra(EXTRA_DESCRIPTION)
        val createdAt = intent.getStringExtra(EXTRA_CREATED_AT)
        return mapStoryToFavorite(
            StoryResponse(
                id ?: "",
                photoUrl ?: "",
                createdAt ?: "",
                name ?: "",
                description ?: ""
            )
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val EXTRA_STORY = "extra_story"
        const val EXTRA_ID = "id"
        const val EXTRA_NAME = "name"
        const val EXTRA_PHOTO_URL = "photo_url"
        const val EXTRA_DESCRIPTION = "description"
        const val EXTRA_CREATED_AT = "created_at"
    }
}