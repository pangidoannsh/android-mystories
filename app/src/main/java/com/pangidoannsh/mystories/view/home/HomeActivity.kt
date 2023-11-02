package com.pangidoannsh.mystories.view.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.pangidoannsh.mystories.R
import com.pangidoannsh.mystories.databinding.ActivityHomeBinding
import com.pangidoannsh.mystories.view.ViewModelFactory
import com.pangidoannsh.mystories.view.favorite.FavoriteStoriesActivity
import com.pangidoannsh.mystories.view.maps.StoriesMapsActivity
import com.pangidoannsh.mystories.view.settings.SettingsActivity
import com.pangidoannsh.mystories.view.story.createstory.CreateStoryActivity
import com.pangidoannsh.mystories.view.story.StoriesFragment
import java.util.Locale

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private val userViewModel by viewModels<UserViewModel> {
        ViewModelFactory.getInstance(application)
    }

    private val launcherIntentCreateStory = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == CreateStoryActivity.CREATE_STORY_RESULT) {
            val get = result.data?.getBooleanExtra(CreateStoryActivity.EXTRA_CREATE_STORY, false)

            Log.d("HomeActivity", "Check extra boolean : ${get.toString()}")
            if (result.data?.getBooleanExtra(
                    CreateStoryActivity.EXTRA_CREATE_STORY,
                    false
                ) == true
            ) {
                val navHostFragment =
                    supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
                val storiesFragment =
                    navHostFragment.childFragmentManager.fragments.firstOrNull { it is StoriesFragment } as StoriesFragment?
                Log.i(
                    HomeActivity::class.java.simpleName,
                    "check fragmetn : ${storiesFragment.toString()}"
                )
                finish()
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setupObserver()
        setupAction()
    }

    private fun setupAction() {
        binding.btnAddStory.setOnClickListener {
            val intent = Intent(this, CreateStoryActivity::class.java)
            launcherIntentCreateStory.launch(intent)
        }
        binding.btnMenu.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
        binding.btnToFavorite.setOnClickListener {
            startActivity(Intent(this, FavoriteStoriesActivity::class.java))
        }
        binding.btnToMap.setOnClickListener {
            startActivity(Intent(this, StoriesMapsActivity::class.java))
        }

    }

    private fun setupObserver() {
        userViewModel.userName.observe(this) { name ->
            binding.greetings.text = getString(R.string.greeting, name.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.getDefault()
                ) else it.toString()
            })
        }
    }
}