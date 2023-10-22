package com.pangidoannsh.mystories.view.story

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.pangidoannsh.mystories.R
import com.pangidoannsh.mystories.databinding.ActivityCreateStoryBinding
import com.pangidoannsh.mystories.view.ViewModelFactory
import com.pangidoannsh.mystories.view.camera.CameraActivity
import com.pangidoannsh.mystories.view.camera.CameraActivity.Companion.CAMERAX_RESULT

class CreateStoryActivity : AppCompatActivity() {
    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
        const val CREATE_STORY_RESULT = 201
        const val EXTRA_CREATE_STORY = "ExtraCreateStory"
    }

    private var _binding: ActivityCreateStoryBinding? = null
    private val binding get() = _binding

    private var currentImageUri: Uri? = null

    private val viewModel by viewModels<StoriesViewModel> {
        ViewModelFactory.getInstance(application)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(
                    this,
                    getString(R.string.permission_request_granted),
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.permission_request_granted),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCreateStoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(getDrawable(R.drawable.ic_back_arrow));

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        setupAction()
        setupObserver()

    }

    private fun setupObserver() {
        viewModel.isCreated.observe(this) {
            if (it) {
                val intent = Intent()
                intent.putExtra(EXTRA_CREATE_STORY, it)
                setResult(CREATE_STORY_RESULT, intent)
                finish()
            }
        }
        viewModel.isLoading.observe(this) { isLoading ->
            binding?.loadingBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
        viewModel.mesasge.observe(this) {
            if (it.isNotEmpty()) Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }
    }

    private fun setupAction() {
        binding?.let { bind ->
            bind.btnSubmitStory.setOnClickListener {
                val description = binding?.etDescription?.text.toString()
                viewModel.submitNewStory(description, currentImageUri ?: null)
            }
            bind.btnOpenGallery.setOnClickListener { startGallery() }
            bind.btnOpenCamera.setOnClickListener { startCameraX() }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding?.imagePreview?.setImageURI(it)
        }
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERAX_RESULT) {
            currentImageUri = it.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
            showImage()
        }
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }


}