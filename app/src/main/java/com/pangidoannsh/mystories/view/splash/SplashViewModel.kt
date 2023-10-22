package com.pangidoannsh.mystories.view.splash

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pangidoannsh.mystories.data.api.ApiConfig

import com.pangidoannsh.mystories.data.local.preerences.UserPreferences

class SplashViewModel(application: Application) : ViewModel() {
    private val _token = MutableLiveData<String>()
    val token: LiveData<String> = _token

    init {
        val source = UserPreferences.getInstance(application).getToken()
        if ((source ?: "").isNotEmpty()) {
            ApiConfig.setToken(source ?: "")
        }
        _token.value = source ?: ""
    }

}