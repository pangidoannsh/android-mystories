package com.pangidoannsh.mystories.view.story.createstory

import android.app.Application
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.pangidoannsh.mystories.R
import com.pangidoannsh.mystories.data.api.ApiConfig
import com.pangidoannsh.mystories.data.api.response.CreateStoryResponse
import com.pangidoannsh.mystories.reduceFileImage
import com.pangidoannsh.mystories.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateStoryViewModel(private val application: Application):ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _isCreated = MutableLiveData<Boolean>()
    val isCreated: LiveData<Boolean> = _isCreated

    private val latLngStory = MutableLiveData<LatLng>()

    val locationCaptured = MutableLiveData(false)

    fun setLocationCaptured(isCaptured: Boolean) {
        locationCaptured.value = isCaptured
    }

    fun setLatLngStory(lat: Double, lng: Double) {
        latLngStory.value = LatLng(lat, lng)
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
            val isUseLocation = locationCaptured.value ?: false
            val imageFile = uriToFile(imageUri, application).reduceFileImage()

            val requestBody = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())

            val lat = latLngStory.value?.latitude
            val lon = latLngStory.value?.longitude

            val requestLat = lat.toString().toRequestBody("text/plain".toMediaType())
            val requestLon = lon.toString().toRequestBody("text/plain".toMediaType())

            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                requestImageFile
            )

            val apiService = ApiConfig.getApiService()

            val client = if (isUseLocation) apiService.createStory(
                multipartBody,
                requestBody,
                requestLat,
                requestLon
            ) else apiService.createStory(multipartBody, requestBody)
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
//                            Log.e("StoriesFragment", "Gagal")
                            setMessage("application.getString(R.string.failure_create_story)")
                        }
                    }
                }

                override fun onFailure(call: Call<CreateStoryResponse>, t: Throwable) {
//                    Log.e("StoriesFragment", t.message.toString())

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
        _message.value = message
    }

    private fun setIsCreated(created: Boolean) {
        _isCreated.value = created
    }

}