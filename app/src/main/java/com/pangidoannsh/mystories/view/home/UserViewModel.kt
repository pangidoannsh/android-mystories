package com.pangidoannsh.mystories.view.home

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pangidoannsh.mystories.data.local.preerences.UserPreferences

class UserViewModel(application: Application) : ViewModel() {
    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> = _userName


    init {
        val userPreferences = UserPreferences.getInstance(application)
        _userName.value = userPreferences.getUserName()
    }
}