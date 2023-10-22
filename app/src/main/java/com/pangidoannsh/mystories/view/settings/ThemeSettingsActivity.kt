package com.pangidoannsh.mystories.view.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import com.pangidoannsh.mystories.R
import com.pangidoannsh.mystories.databinding.ActivityThemeSettingsBinding
import com.pangidoannsh.mystories.view.ViewModelFactory

class ThemeSettingsActivity : AppCompatActivity() {
    private var _binding: ActivityThemeSettingsBinding? = null
    private val binding get() = _binding

    private val viewModel by viewModels<SettingsViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityThemeSettingsBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        supportActionBar?.let { appBar ->
            appBar.title = getString(R.string.settings_theme_title)
            appBar.setDisplayHomeAsUpEnabled(true)
        }
        setupComponent()
    }

    private fun setupComponent() {
        binding?.let { bind ->
            viewModel.getThemeSettings().observe(this) {
                bind.rbIsDarkMode.isChecked = it
                bind.rbIsNonDarkMode.isChecked = !it
            }
            bind.rbIsDarkMode.setOnClickListener {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                viewModel.setTheme(true)
            }
            bind.rbIsNonDarkMode.setOnClickListener {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                viewModel.setTheme(false)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}