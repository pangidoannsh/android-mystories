package com.pangidoannsh.mystories.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.pangidoannsh.mystories.data.local.entity.FavoriteStories
import com.pangidoannsh.mystories.data.local.room.StoriesDao
import com.pangidoannsh.mystories.data.local.room.StoriesDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepository(application: Application) {
    private val storiesDao: StoriesDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = StoriesDatabase.getInstance(application)
        storiesDao = db.storiesDao()
    }

    fun getAllFavoriteStories(): LiveData<List<FavoriteStories>> = storiesDao.getFavoriteStories()

    fun getFavoriteStory(storyId: String): LiveData<FavoriteStories> = storiesDao.getStory(storyId)

    fun addFavorite(story: FavoriteStories) {
        executorService.execute {
            storiesDao.insertFavorite(story)
        }
    }

    fun isFavoriteStory(id: String): LiveData<Boolean> = storiesDao.isFavoriteStory(id)

    fun deleteFromFavorite(storyId: String) {
        executorService.execute { storiesDao.deleteFavorite(storyId) }
    }

    companion object {
        @Volatile
        private var INSTANCE: FavoriteRepository? = null

        @JvmStatic
        fun getInstance(application: Application): FavoriteRepository {
            if (INSTANCE == null) {
                synchronized(FavoriteRepository::class.java) {
                    INSTANCE = FavoriteRepository(application)
                }
            }
            return INSTANCE as FavoriteRepository
        }
    }

}