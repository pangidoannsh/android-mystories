package com.pangidoannsh.mystories.view.auth

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.pangidoannsh.mystories.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)

        setContentView(binding.root)
        supportActionBar?.hide()

        setupAnim()

    }

    private fun setupAnim() {

        val scaleX = ObjectAnimator.ofFloat(binding.appLogo, View.SCALE_X, 1.1f, 1f).apply {
            duration = 1000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }
        val scaleY = ObjectAnimator.ofFloat(binding.appLogo, View.SCALE_Y, 1.1f, 1f).apply {
            duration = 1000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }

        AnimatorSet().apply {
            playTogether(scaleX, scaleY)
            start()
        }
        ObjectAnimator.ofFloat(binding.fragmentContainer, View.TRANSLATION_Y, 250f, 0f).apply {
            duration = 500
        }.start()
    }
}