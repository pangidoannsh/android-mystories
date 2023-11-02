package com.pangidoannsh.mystories.view.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.pangidoannsh.mystories.data.api.response.StoryResponse
import com.pangidoannsh.mystories.data.repository.StoriesRepository

class StoriesViewModel(private val storyRepo: StoriesRepository) : ViewModel() {
    val stories: LiveData<PagingData<StoryResponse>> =
        storyRepo.getStoriesPaging().cachedIn(viewModelScope)
}