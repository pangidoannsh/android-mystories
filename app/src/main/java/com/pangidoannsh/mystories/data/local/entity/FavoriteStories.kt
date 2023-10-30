package com.pangidoannsh.mystories.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pangidoannsh.mystories.data.StoryInterface
import com.pangidoannsh.mystories.data.api.response.StoryResponse
import kotlinx.parcelize.Parcelize

@Entity(tableName = "favorite_stories")
@Parcelize
data class FavoriteStories(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    override var id: String,

    @ColumnInfo(name = "name")
    override var name: String,

    @ColumnInfo(name = "description")
    override var description: String,

    @ColumnInfo(name = "photoUrl")
    override var photoUrl: String? = null,

    @ColumnInfo(name = "createdAt")
    override  val createdAt: String
) : Parcelable, StoryInterface