package com.pangidoannsh.mystories.view.settings

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.pangidoannsh.mystories.data.local.preerences.SettingPreferences
import com.pangidoannsh.mystories.data.local.preerences.UserPreferences
import com.pangidoannsh.mystories.data.local.preerences.dataStore
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : ViewModel() {
    private val userPreferences = UserPreferences.getInstance(application)
    private var settingPref: SettingPreferences =
        SettingPreferences.getInstance(application.dataStore)

    fun getThemeSettings(): LiveData<Boolean> {
        return settingPref.getThemeSetting().asLiveData()
    }

    fun setTheme(newIsDarkMode: Boolean) {
        viewModelScope.launch {
            settingPref.saveThemeSetting(newIsDarkMode)
        }
    }

    fun logout() {
        userPreferences.clearUser()
    }
}