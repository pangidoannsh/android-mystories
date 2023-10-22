package com.pangidoannsh.mystories.view.auth

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pangidoannsh.mystories.R
import com.pangidoannsh.mystories.data.api.ApiConfig
import com.pangidoannsh.mystories.data.api.payload.Login
import com.pangidoannsh.mystories.data.api.response.LoginResponse
import com.pangidoannsh.mystories.data.local.preerences.UserPreferences
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val application: Application) : ViewModel() {

    private val pref: UserPreferences = UserPreferences.getInstance(application)

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isLogin = MutableLiveData<Boolean>()
    val isLogin: LiveData<Boolean> = _isLogin

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private fun saveToken(newToken: String, userName: String, userId: String) {
        viewModelScope.launch {
            ApiConfig.setToken(newToken)
            pref.saveToken(newToken)
            pref.saveUserName(userName)
            pref.saveUserId(userId)
            _isLogin.value = true
        }
    }


    init {
        _isLoading.value = false
    }

    fun submitLogin(email: String, password: String) {
        Log.d("FiturLogin", Login(email, password).toString())
        val client = ApiConfig.getApiService().login(Login(email, password))
        _isLoading.value = true
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                _isLoading.value = false

                val responseData = response.body()

                Log.d("FiturLogin", response.toString())
//                Log.i("FiturLogin", "Is Successful : ${responseData?.message ?: "UNDIFINED"}")

                if (response.isSuccessful) {
                    _message.value = application.getString(R.string.success_login_message)
                    saveToken(
                        responseData?.loginResult?.token ?: "",
                        responseData?.loginResult?.name ?: "undifined name",
                        responseData?.loginResult?.userId ?: ""
                    )


                } else {
                    _message.value = application.getString(R.string.failure_login_message)
                }

            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _message.value = application.getString(R.string.failure_login_message)
                _isLoading.value = false
                Log.e("FiturLogin", "Error : ${t}")
            }

        })
    }
}