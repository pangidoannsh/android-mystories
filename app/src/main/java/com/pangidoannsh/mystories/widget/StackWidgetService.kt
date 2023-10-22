package com.pangidoannsh.mystories.widget

import android.content.Intent
import android.os.Build
import android.widget.RemoteViewsService
import com.pangidoannsh.mystories.data.api.response.StoriesResponse
import com.pangidoannsh.mystories.data.api.response.StoryResponse

class StackWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        val listStories = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_STORIES, StoriesResponse::class.java)
        } else null

        return StackRemoteViewsFactory(this.applicationContext,listStories?.listStory)
    }

    companion object{
        const val EXTRA_STORIES = "extra_stories"
    }
}