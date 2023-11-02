package com.pangidoannsh.mystories.view.favorite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.pangidoannsh.mystories.data.local.entity.FavoriteStories
import com.pangidoannsh.mystories.data.repository.FavoriteRepository
import com.pangidoannsh.mystories.data.repository.StoriesRepository

class FavoriteStoriesViewModel(private val repository: FavoriteRepository) : ViewModel() {

    fun getFavoriteStories(): LiveData<List<FavoriteStories>> = repository.getAllFavoriteStories()

    fun deleteFromFavoriteStories(storyId: String) {
        repository.deleteFromFavorite(storyId)
    }
}