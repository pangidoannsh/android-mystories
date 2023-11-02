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

class DetailStoryViewModel(
    private val favoriteRepo:FavoriteRepository
) :
    ViewModel() {

    private val _story = MutableLiveData<FavoriteStories>()
    val storyData: LiveData<FavoriteStories> = _story

    fun initStoryData(initStory: FavoriteStories) {
        _story.value = initStory
        getStoryDetail()
    }

    val isFavoriteStory = storyData.switchMap {
        favoriteRepo.isFavoriteStory(it.id)
    }

    fun changeStatusFavorite() {
        storyData.value?.let {
            val story = FavoriteStories(
                id = it.id,
                name = it.name,
                photoUrl = it.photoUrl,
                description = it.description,
                createdAt = it.createdAt,
            )
            if (isFavoriteStory.value as Boolean) {
                favoriteRepo.deleteFromFavorite(it.id)
            } else {
                favoriteRepo.addFavorite(story)
            }
        }
    }

//    fun getFavoriteStory() = storyData.value?.let { favoriteRepo.getFavoriteStory(it.id) }


    private fun getStoryDetail() {
        storyData.value?.let { thisStory ->
            val client = ApiConfig.getApiService().getStoryById(thisStory.id)
            client.enqueue(object : Callback<DetailStoryResponse> {
                override fun onResponse(
                    call: Call<DetailStoryResponse>,
                    response: Response<DetailStoryResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            _story.value = FavoriteStories(
                                id = it.story.id,
                                name = it.story.name,
                                photoUrl = it.story.photoUrl,
                                createdAt = it.story.createdAt,
                                description = it.story.description
                            )
                        }
                    } else {
                        Log.e(
                            "DetailStoryViewModel",
                            response.body()?.message ?: "Error tidak diketahui"
                        )
                    }
                }

                override fun onFailure(call: Call<DetailStoryResponse>, t: Throwable) {
                    Log.e("DetailStoryViewModel", t.message.toString())
                }

            })
        }
    }
}