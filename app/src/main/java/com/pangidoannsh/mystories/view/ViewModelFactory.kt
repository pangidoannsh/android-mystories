package com.pangidoannsh.mystories.view

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pangidoannsh.mystories.view.auth.LoginViewModel
import com.pangidoannsh.mystories.view.favorite.FavoriteStoriesViewModel
import com.pangidoannsh.mystories.view.home.UserViewModel
import com.pangidoannsh.mystories.view.settings.SettingsViewModel
import com.pangidoannsh.mystories.view.splash.SplashViewModel
import com.pangidoannsh.mystories.view.story.StoriesViewModel
import com.pangidoannsh.mystories.view.story.detailstory.DetailStoryViewModel

class ViewModelFactory private constructor(private val application: Application) :
    ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(application: Application): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(application)
                }
            }
            return INSTANCE as ViewModelFactory
        }

    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SplashViewModel::class.java)) {
            return SplashViewModel(application) as T
        } else if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(application) as T
        } else if (modelClass.isAssignableFrom(StoriesViewModel::class.java)) {
            return StoriesViewModel(application) as T
        } else if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(application) as T
        } else if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(application) as T
        } else if (modelClass.isAssignableFrom(FavoriteStoriesViewModel::class.java)) {
            return FavoriteStoriesViewModel(application) as T
        } else if (modelClass.isAssignableFrom(DetailStoryViewModel::class.java)) {
            return DetailStoryViewModel(application) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}