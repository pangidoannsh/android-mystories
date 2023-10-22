package com.pangidoannsh.mystories.view.auth

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pangidoannsh.mystories.R
import com.pangidoannsh.mystories.data.api.ApiConfig
import com.pangidoannsh.mystories.data.api.payload.Register
import com.pangidoannsh.mystories.data.api.response.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError
    init {
        _isLoading.value = false
        _isSuccess.value = false
    }

    fun submitRegister(name: String, email: String, password: String) {
        val payload = Register(name, email, password)

        _isLoading.value = true
        val client = ApiConfig.getApiService().register(payload)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                _isLoading.value = false
                val responseBody = response.body()

                if (response.isSuccessful) {
                    responseBody?.let { res ->
                        setMessage(res.message)
                    }
                    _isSuccess.value = true
                } else {
                    _isError.value = true
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _isLoading.value = false
                t?.let {
                    setMessage(
                        it.message ?: Resources.getSystem()
                            .getString(R.string.error_register_message)
                    )
                }
            }

        })
    }

    private fun setMessage(newMessage: String) {
        _message.value = newMessage
    }
}