package com.pangidoannsh.mystories.view.maps

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.pangidoannsh.mystories.R
import com.pangidoannsh.mystories.data.api.ApiConfig
import com.pangidoannsh.mystories.data.api.response.DetailStoryResponse
import com.pangidoannsh.mystories.data.api.response.StoriesResponse
import com.pangidoannsh.mystories.data.api.response.StoryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoriesMapViewModel(private val application: Application) : ViewModel() {

    private val _stories = MutableLiveData<List<StoryResponse>>();
    val stories: LiveData<List<StoryResponse>> = _stories

    private val _detailStory = MutableLiveData<StoryResponse>();
    val detailStory: LiveData<StoryResponse> = _detailStory

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    init {
        getStories()
    }

    private fun getStories() {
        _loading.value = true

        val client = ApiConfig.getApiService().getStories(30, location = 1)
        client.enqueue(object : Callback<StoriesResponse> {
            override fun onResponse(
                call: Call<StoriesResponse>,
                response: Response<StoriesResponse>
            ) {
                _loading.value = false
                if (response.isSuccessful) {
                    val resBody = response.body()
                    resBody?.let {
                        _stories.value = it.listStory
                    }
                } else {
                    _message.value = application.getString(R.string.failure_get_list_stories)
                }
            }

            override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                _loading.value = false
                _message.value = t.message.toString()
            }

        })
    }

    fun getDetailStory(id: String) {
        val client = ApiConfig.getApiService().getStoryById(id)
        client.enqueue(object : Callback<DetailStoryResponse> {
            override fun onResponse(
                call: Call<DetailStoryResponse>,
                response: Response<DetailStoryResponse>
            ) {
                if (response.isSuccessful) {
                    val resBody = response.body()
                    resBody?.let {
                        _detailStory.value = it.story
                    }
                }
            }

            override fun onFailure(call: Call<DetailStoryResponse>, t: Throwable) {

            }


        })
    }
}