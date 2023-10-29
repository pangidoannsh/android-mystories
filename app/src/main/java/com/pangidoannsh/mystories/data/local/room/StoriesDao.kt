package com.pangidoannsh.mystories.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.pangidoannsh.mystories.data.local.entity.FavoriteStories

@Dao
interface StoriesDao {
    @Query("SELECT * FROM favorite_stories ORDER BY createdAt DESC")
    fun getFavoriteStories(): LiveData<List<FavoriteStories>>

    @Query("SELECT * FROM favorite_stories WHERE id= :id")
    fun getStory(id: String): LiveData<FavoriteStories>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertFavorite(story: FavoriteStories)

    @Query("DELETE FROM favorite_stories WHERE id= :id")
    fun deleteFavorite(id: String)

}