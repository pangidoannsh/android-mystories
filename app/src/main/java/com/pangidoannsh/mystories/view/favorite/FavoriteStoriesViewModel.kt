package com.pangidoannsh.mystories.view.favorite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.pangidoannsh.mystories.data.local.entity.FavoriteStories
import com.pangidoannsh.mystories.data.repository.FavoriteRepository

class FavoriteStoriesViewModel(application: Application) : ViewModel() {
    private val repository = FavoriteRepository.getInstance(application)

    fun getFavoriteStories(): LiveData<List<FavoriteStories>> = repository.getAllFavoriteStories()

    fun deletFromFavoriteStories(storyId: String) {
        repository.deleteFromFavorite(storyId)
    }
}