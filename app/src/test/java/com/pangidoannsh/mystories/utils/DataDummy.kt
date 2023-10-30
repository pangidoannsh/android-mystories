package com.pangidoannsh.mystories.utils

import com.pangidoannsh.mystories.data.api.response.StoryResponse
import com.pangidoannsh.mystories.data.local.entity.FavoriteStories

object DataDummy {
    fun generateDummyFavoriteStories(): List<FavoriteStories> {
        val stories = ArrayList<FavoriteStories>()
        for (i in 0..10) {
            stories.add(generateDummyFavorite("story-$i"))
        }
        return stories
    }

    fun generateDummyFavorite(dummyId: String, dummyName: String? = null): FavoriteStories {
        val story = generateStory(dummyId, dummyName)
        return FavoriteStories(
            id = story.id,
            name = story.name,
            photoUrl = story.photoUrl,
            description = story.description,
            createdAt = story.createdAt
        )

    }

    fun generateStory(dummyId: String, dummyName: String? = null): StoryResponse {
        return StoryResponse(
            id = dummyId,
            photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1698600320055_Sd08PqTY.jpg",
            name = dummyName ?: "dummy name",
            description = "lorem ipsum dummy description",
            createdAt = "2022-02-22T22:22:22Z"
        )
    }

    fun generateListStories(): List<StoryResponse> {
        val stories: MutableList<StoryResponse> = arrayListOf()
        for (i in 0..100) {
            stories.add(generateStory("story-$i"))
        }
        return stories
    }

}