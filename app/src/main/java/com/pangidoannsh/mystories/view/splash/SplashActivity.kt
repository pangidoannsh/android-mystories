package com.pangidoannsh.mystories.view.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import com.pangidoannsh.mystories.R
import com.pangidoannsh.mystories.view.ViewModelFactory
import com.pangidoannsh.mystories.view.auth.AuthActivity
import com.pangidoannsh.mystories.view.home.HomeActivity
import com.pangidoannsh.mystories.view.settings.SettingsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    private val activityScope = CoroutineScope(Dispatchers.Main)

    private val viewModel by viewModels<SplashViewModel> {
        ViewModelFactory.getInstance(application)
    }
    private val settingViewModel by viewModels<SettingsViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()

        var isLogin = false

        val toHome = Intent(this, HomeActivity::class.java)
        val toAuth = Intent(this, AuthActivity::class.java)

        viewModel.token.observe(this) {
            if (it.isNotEmpty()) isLogin = true
        }
        settingViewModel.getThemeSettings().observe(this) {
            if (it) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        activityScope.launch {
            delay(2000L)
            runOnUiThread {
                if (isLogin) {
                    startActivity(toHome)
                } else {
                    startActivity(toAuth)
                }
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        activityScope.coroutineContext.cancelChildren()
    }
}