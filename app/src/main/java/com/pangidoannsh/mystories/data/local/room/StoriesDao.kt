package com.pangidoannsh.mystories.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.pangidoannsh.mystories.data.local.entity.FavoriteStories

@Dao
interface StoriesDao {
    @Query("SELECT EXISTS(SELECT * FROM FAVORITE_STORIES WHERE id = :id)")
    fun isFavoriteStory(id: String): LiveData<Boolean>

    @Query("SELECT * FROM favorite_stories ORDER BY createdAt DESC")
    fun getFavoriteStories(): LiveData<List<FavoriteStories>>

    @Query("SELECT * FROM favorite_stories WHERE id= :id")
    fun getStory(id: String): LiveData<FavoriteStories>

    @Query("SELECT EXISTS(SELECT * FROM favorite_stories WHERE id = :id)")
    fun isStoryFavorite(id: String): LiveData<Boolean>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertFavorite(story: FavoriteStories)

    @Query("DELETE FROM favorite_stories WHERE id= :id")
    fun deleteFavorite(id: String)

}