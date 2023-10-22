package com.pangidoannsh.mystories.view.story.detailstory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pangidoannsh.mystories.data.local.entity.FavoriteStories
import com.pangidoannsh.mystories.view.favorite.FavoriteStoriesViewModel

class DetailStoryViewModelFactory private constructor(
    private val application: Application,
    story: FavoriteStories
) :
    ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var INSTANCE: DetailStoryViewModelFactory? = null

        @JvmStatic
        fun getInstance(
            application: Application,
            story: FavoriteStories
        ): DetailStoryViewModelFactory {
            if (INSTANCE == null) {
                synchronized(DetailStoryViewModelFactory::class.java) {
                    INSTANCE = DetailStoryViewModelFactory(application, story)
                }
            }
            return INSTANCE as DetailStoryViewModelFactory
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteStoriesViewModel::class.java)) {
            return FavoriteStoriesViewModel(application) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}