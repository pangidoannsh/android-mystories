package com.pangidoannsh.mystories.view.settings

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.pangidoannsh.mystories.R
import com.pangidoannsh.mystories.databinding.ActivitySettingsBinding
import com.pangidoannsh.mystories.view.ViewModelFactory
import com.pangidoannsh.mystories.view.auth.AuthActivity
import java.util.Locale

class SettingsActivity : AppCompatActivity() {

    private var _binding: ActivitySettingsBinding? = null
    private val binding get() = _binding

    private val viewModel by viewModels<SettingsViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        supportActionBar?.let { appBar ->
            appBar.title = getString(R.string.settings_title)
            appBar.setDisplayHomeAsUpEnabled(true)
        }

        setupValue()
        setupAction()
    }

    private fun setupValue() {
        binding?.languageValue?.text = Locale.getDefault().language

        viewModel.getThemeSettings().observe(this) {
            binding?.nightModeValue?.text =
                if (it) getString(R.string.active) else getString(R.string.non_active)
        }
    }

    private fun setupAction() {
        binding?.let { bind ->
            bind.btnLanguage.setOnClickListener {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }
            bind.btnNightMode.setOnClickListener {
                startActivity(
                    Intent(
                        this,
                        ThemeSettingsActivity::class.java
                    )
                )
            }
            bind.btnLogout.setOnClickListener {
                showConfirmationLogout()
//                viewModel.logout()
//                startActivity(Intent(this, AuthActivity::class.java))
//                finishAffinity()
            }
        }
    }

    private fun showConfirmationLogout() {
        val toLoginIntent = Intent(this, AuthActivity::class.java)
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.apply {
            setTitle(getString(R.string.label_logout))
            setIcon(getDrawable(R.drawable.ic_logout))
            setMessage(getString(R.string.logout_confirmation))
            setPositiveButton(getString(R.string.yes)) { dialog, id ->
                viewModel.logout()
                startActivity(toLoginIntent)
                finishAffinity()
            }
            setNegativeButton(getString(R.string.cancel)) { dialog, id ->
                dialog.dismiss()
            }
        }
        val dialog = alertDialog.create()
        dialog.show()

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}