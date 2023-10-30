package com.pangidoannsh.mystories.view.story.detailstory

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.pangidoannsh.mystories.data.api.ApiConfig
import com.pangidoannsh.mystories.data.api.response.DetailStoryResponse
import com.pangidoannsh.mystories.data.local.entity.FavoriteStories
import com.pangidoannsh.mystories.data.repository.FavoriteRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailStoryViewModel(private val favoriteRepo: FavoriteRepository) :
    ViewModel() {
//    private val favoriteRepo = FavoriteRepository.getInstance(application)

    private val _story = MutableLiveData<FavoriteStories>()
    private val storyData: LiveData<FavoriteStories> = _story

    fun initStoryData(initStory: FavoriteStories) {
        _story.value = initStory
//        getStoryDetail()
    }

    val isFavorite = storyData.switchMap {
        favoriteRepo.isStoryFavorite(it.id)
    }

    fun changeFavoriteStatus() {
        storyData.value?.let {
            val story = FavoriteStories(
                id = it.id,
                name = it.name,
                photoUrl = it.photoUrl,
                description = it.description,
                createdAt = it.createdAt,
            )

            if (isFavorite.value as Boolean) {
                favoriteRepo.deleteFromFavorite(it.id)
            } else {
                favoriteRepo.addFavorite(story)
            }
        }
    }

    fun setFavorite() {
        storyData.value?.let {
            val story = FavoriteStories(
                id = it.id,
                name = it.name,
                photoUrl = it.photoUrl,
                description = it.description,
                createdAt = it.createdAt,
            )
            favoriteRepo.addFavorite(story)
        }
    }

    fun removeFavorite() {
        storyData.value?.let {
            favoriteRepo.deleteFromFavorite(it.id)
        }
    }

//    private fun getStoryDetail() {
//        storyData.value?.let { thisStory ->
//            val client = ApiConfig.getApiService().getStoryById(thisStory.id)
//            client.enqueue(object : Callback<DetailStoryResponse> {
//                override fun onResponse(
//                    call: Call<DetailStoryResponse>,
//                    response: Response<DetailStoryResponse>
//                ) {
//                    if (response.isSuccessful) {
//                        response.body()?.let {
//                            _story.value = FavoriteStories(
//                                id = it.story.id,
//                                name = it.story.name,
//                                photoUrl = it.story.photoUrl,
//                                createdAt = it.story.createdAt,
//                                description = it.story.description
//                            )
//                        }
//                    } else {
//                        Log.e(
//                            "DetailStoryViewModel",
//                            response.body()?.message ?: "Error tidak diketahui"
//                        )
//                    }
//                }
//
//                override fun onFailure(call: Call<DetailStoryResponse>, t: Throwable) {
//                    Log.e("DetailStoryViewModel", t.message.toString())
//                }
//
//            })
//        }
//    }
}