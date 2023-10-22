package com.pangidoannsh.mystories.view.story

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pangidoannsh.mystories.R
import com.pangidoannsh.mystories.data.api.ApiConfig
import com.pangidoannsh.mystories.data.api.response.CreateStoryResponse
import com.pangidoannsh.mystories.data.api.response.StoriesResponse
import com.pangidoannsh.mystories.data.api.response.StoryResponse
import com.pangidoannsh.mystories.reduceFileImage
import com.pangidoannsh.mystories.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoriesViewModel(private val application: Application) : ViewModel() {
    private val _listStories = MutableLiveData<List<StoryResponse>>()
    val listStories: LiveData<List<StoryResponse>> = _listStories

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _mesasge = MutableLiveData<String>()
    val mesasge: LiveData<String> = _mesasge

    private val _isCreated = MutableLiveData<Boolean>()
    val isCreated: LiveData<Boolean> = _isCreated

    fun getListStories() {
        setIsLoading(true)
        val client = ApiConfig.getApiService().getStories()

        client.enqueue(object : Callback<StoriesResponse> {
            override fun onResponse(
                call: Call<StoriesResponse>,
                response: Response<StoriesResponse>
            ) {
                setIsLoading(false)

                val resData = response.body()
                resData?.let { data ->
                    if (response.isSuccessful) {
                        setListStories(data.listStory)
                    } else {
                        Log.d("StoriesViewModel", response.headers().toString())
                        setMessage(
                            data.message ?: application.getString(R.string.failure_get_list_stories)
                        )
                    }
                }
            }

            override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                setIsLoading(false)
                setMessage(t.message.toString())
            }

        })
    }

    fun submitNewStory(description: String, imageUri: Uri?) {
        if (description.isEmpty()) {
            setMessage(application.getString(R.string.empty_description_onsubmit))
            return
        }
        if (imageUri === null) {
            setMessage(application.getString(R.string.empty_image_onsubmit))
            return
        }
        imageUri?.let {
            setIsLoading(true)
            val imageFile = uriToFile(imageUri, application).reduceFileImage()

            val requestBody = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                requestImageFile
            )
            val client = ApiConfig.getApiService().createStory(multipartBody, requestBody)
            client.enqueue(object : Callback<CreateStoryResponse> {
                override fun onResponse(
                    call: Call<CreateStoryResponse>,
                    response: Response<CreateStoryResponse>
                ) {
                    setIsLoading(false)

                    val resData = response.body()

                    resData?.let {
                        if (response.isSuccessful) {
                            setMessage(application.getString(R.string.success_create_story))
                            setIsCreated(true)
                        } else {
                            Log.e("StoriesFragment", "Gagal")
                            setMessage("application.getString(R.string.failure_create_story)")
                        }
                    }
                }

                override fun onFailure(call: Call<CreateStoryResponse>, t: Throwable) {
                    Log.e("StoriesFragment", t.message.toString())

                    setIsLoading(false)
                    setMessage(t.message.toString())
                }

            })
        }

    }

    private fun setIsLoading(loading: Boolean) {
        _isLoading.value = loading
    }

    private fun setMessage(message: String) {
        _mesasge.value = message
    }

    private fun setListStories(stories: List<StoryResponse>) {
        _listStories.value = stories
    }

    private fun setIsCreated(created: Boolean) {
        _isCreated.value = created
    }
}