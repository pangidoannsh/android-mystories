package com.pangidoannsh.mystories.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.pangidoannsh.mystories.data.pagingsource.StoriesPagingSource
import com.pangidoannsh.mystories.data.api.ApiConfig
import com.pangidoannsh.mystories.data.api.ApiService
import com.pangidoannsh.mystories.data.api.response.StoryResponse

class StoriesRepository {
    private val apiService: ApiService = ApiConfig.getApiService()

    fun getStoriesPaging(): LiveData<PagingData<StoryResponse>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoriesPagingSource(apiService)
            }
        ).liveData
    }
}