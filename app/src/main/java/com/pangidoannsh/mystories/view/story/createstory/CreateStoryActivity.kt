package com.pangidoannsh.mystories.view.story.createstory

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices
import com.pangidoannsh.mystories.view.story.StoriesViewModel

class CreateStoryActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var _binding: ActivityCreateStoryBinding? = null
    private val binding get() = _binding

    private var currentImageUri: Uri? = null

    private val viewModel by viewModels<CreateStoryViewModel> {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCreateStoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(getDrawable(R.drawable.ic_back_arrow));
        binding?.let {
            it.toolbar.setNavigationOnClickListener {
                finish()
            }
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (!checkPermission(REQUIRED_PERMISSION)) {
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
        viewModel.message.observe(this) {
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
            bind.cbUseLocation.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    getMyLocation()
                } else {
                    viewModel.setLocationCaptured(false)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding?.let { bind ->
                bind.imagePreview.setImageURI(it)
            }
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

    private val reqLocationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->

        val accessFineLocation = permissions[ACCESS_FINE_LOCATION] ?: false
        val accessCoarseLocation = permissions[ACCESS_COARSE_LOCATION] ?: false

        if (accessFineLocation && accessCoarseLocation) {
            getMyLocation()
        }
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getMyLocation() {
        if (checkPermission(ACCESS_FINE_LOCATION) && checkPermission(ACCESS_COARSE_LOCATION)) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    Log.d("TestLatLng", "${location.latitude} | ${location.longitude}")

                    viewModel.setLatLngStory(location.latitude, location.longitude)
                    viewModel.setLocationCaptured(true)
                } else {
//                    viewModel.setLocationCaptured(false)
                    Toast.makeText(this, getString(R.string.cant_find_location), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        } else {
            reqLocationPermissionLauncher.launch(
                arrayOf(
                    ACCESS_FINE_LOCATION,
                    ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
        private const val ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
        private const val ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
        const val CREATE_STORY_RESULT = 201
        const val EXTRA_CREATE_STORY = "ExtraCreateStory"
    }
}