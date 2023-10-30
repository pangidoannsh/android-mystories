package com.pangidoannsh.mystories.view.story

import android.app.Application
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.google.android.gms.maps.model.LatLng
import com.pangidoannsh.mystories.R
import com.pangidoannsh.mystories.data.api.ApiConfig
import com.pangidoannsh.mystories.data.api.response.CreateStoryResponse
import com.pangidoannsh.mystories.data.api.response.StoriesResponse
import com.pangidoannsh.mystories.data.api.response.StoryResponse
import com.pangidoannsh.mystories.data.repository.StoriesRepository
import com.pangidoannsh.mystories.reduceFileImage
import com.pangidoannsh.mystories.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoriesViewModel(
    private val storiesRepo: StoriesRepository
) : ViewModel() {

    var stories: LiveData<PagingData<StoryResponse>> =
        storiesRepo.getStoriesPaging().cachedIn(viewModelScope)

//    private val _listStories = MutableLiveData<List<StoryResponse>>()
//    val listStories: LiveData<List<StoryResponse>> = _listStories

//    private val _isLoading = MutableLiveData<Boolean>()
//    val isLoading: LiveData<Boolean> = _isLoading
//
//    private val _message = MutableLiveData<String>()
//    val message: LiveData<String> = _message

//    fun getListStories() {
//        setIsLoading(true)
//        val client = ApiConfig.getApiService().getStories()
//
//        client.enqueue(object : Callback<StoriesResponse> {
//            override fun onResponse(
//                call: Call<StoriesResponse>,
//                response: Response<StoriesResponse>
//            ) {
//                setIsLoading(false)
//
//                val resData = response.body()
//                resData?.let { data ->
//                    if (response.isSuccessful) {
//                        setListStories(data.listStory)
//                    } else {
////                        Log.d("StoriesViewModel", response.headers().toString())
//                        setMessage(
//                            data.message ?: application.getString(R.string.failure_get_list_stories)
//                        )
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
//                setIsLoading(false)
//                setMessage(t.message.toString())
//            }
//
//        })
//    }



//    private fun setIsLoading(loading: Boolean) {
//        _isLoading.value = loading
//    }
//
//    private fun setMessage(message: String) {
//        _message.value = message
//    }

//    private fun setListStories(stories: List<StoryResponse>) {
//        _listStories.value = stories
//    }

}