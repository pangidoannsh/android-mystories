package com.pangidoannsh.mystories.data.api.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.pangidoannsh.mystories.data.StoryInterface
import kotlinx.parcelize.Parcelize

@Parcelize
data class StoriesResponse(

    @field:SerializedName("error")
    val error: Boolean = true,

    @field:SerializedName("message")
    val message: String? = "",

    @field:SerializedName("listStory")
    val listStory: List<StoryResponse>
) : Parcelable

data class DetailStoryResponse(
    @field:SerializedName("error")
    val error: Boolean = true,

    @field:SerializedName("message")
    val message: String? = "",

    val story: StoryResponse
)

@Parcelize
data class StoryResponse(

    @field:SerializedName("id")
    override val id: String,

    @field:SerializedName("photoUrl")
    override val photoUrl: String? = null,

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("name")
    override val name: String,

    @field:SerializedName("description")
    override val description: String,

    val lat: Double = 0.0,

    val lon: Double = 0.0,
) : Parcelable, StoryInterface

data class CreateStoryResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)
