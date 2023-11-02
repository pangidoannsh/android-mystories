package com.pangidoannsh.mystories.view.utils

import com.pangidoannsh.mystories.data.api.response.StoryResponse
import com.pangidoannsh.mystories.data.local.entity.FavoriteStories
import com.pangidoannsh.mystories.mapStoryToFavorite

object DataDummy {
    fun generateStories(): List<StoryResponse> {
        val stories = ArrayList<StoryResponse>()
        for (i in 0..10) {
            val story = generateStory("dummy-story-$i")
            stories.add(story)
        }
        return stories
    }

    fun generateFavoriteStories(): List<FavoriteStories> {
        val favStories = ArrayList<FavoriteStories>()
        val stories = generateStories()
        stories.forEach {
            favStories.add(mapStoryToFavorite(it))
        }

        return favStories
    }

    fun generateFavoriteStory():FavoriteStories{
        return mapStoryToFavorite((generateStory("dummy-story")))
    }

    fun generateStory(id: String): StoryResponse {
        return StoryResponse(
            id = id,
            name = "Dummy Name",
            description = "Lorem ipsum dollor Dummy",
            createdAt = "2022-02-22T22:22:22Z",
            photoUrl = "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png",
        )
    }
}