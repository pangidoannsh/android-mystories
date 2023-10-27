package com.pangidoannsh.mystories.view.maps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.pangidoannsh.mystories.R
import com.pangidoannsh.mystories.databinding.ActivityStoriesMapsBinding
import com.pangidoannsh.mystories.view.ViewModelFactory
import com.pangidoannsh.mystories.view.story.detailstory.DetailStoryActivity

class StoriesMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityStoriesMapsBinding
    private val viewModel by viewModels<StoriesMapViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoriesMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding?.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(getDrawable(R.drawable.ic_back_arrow));
        binding?.let {
            it.toolbar.setNavigationOnClickListener {
                finish()
            }
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        setupDetailStory()
        setupObserve()
    }

    private fun setupObserve() {
        viewModel.loading.observe(this) {
            binding.loadingBar.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

    private fun setupDetailStory() {
        viewModel.detailStory.observe(this) { story ->
            if (story !== null) {
                if (story.photoUrl !== null) {
                    Glide.with(this)
                        .load(story.photoUrl)
                        .fitCenter()
                        .into(binding.detailImage)
                }
                binding.detailContainer.visibility = View.VISIBLE
                binding.btnToDetail.setOnClickListener {
                    val toDetail = Intent(this, DetailStoryActivity::class.java)
                    toDetail.apply {
                        putExtra(DetailStoryActivity.EXTRA_ID, story.id)
                        putExtra(DetailStoryActivity.EXTRA_NAME, story.name)
                        putExtra(DetailStoryActivity.EXTRA_DESCRIPTION, story.description)
                        putExtra(DetailStoryActivity.EXTRA_PHOTO_URL, story.photoUrl)
                    }


                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            this,
                            Pair(binding.detailImage, "image"),
                        )
                    startActivity(toDetail, optionsCompat.toBundle())
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.map_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.normal_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                true
            }

            R.id.satellite_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                true
            }

            R.id.terrain_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
                true
            }

            R.id.hybrid_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                true
            }

            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        viewModel.stories.observe(this) {
            it.forEach { story ->
                val latLng = LatLng(story.lat, story.lon)
                mMap.addMarker(
                    MarkerOptions()
                        .position(latLng)
                        .title(story.name)
                        .snippet(story.description)
                )?.tag = story.id
            }
        }
        mMap.setOnMapClickListener {
            binding.detailContainer.visibility = View.GONE
        }
        mMap.setOnMarkerClickListener { marker ->
            Log.i("ResponseID", marker.tag.toString())
            marker.tag?.let {
                viewModel.getDetailStory(it.toString())
            }
            false
        }

        val indonesiaPlace = LatLng(-5.0, 110.0)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(indonesiaPlace, 5f))
    }

}