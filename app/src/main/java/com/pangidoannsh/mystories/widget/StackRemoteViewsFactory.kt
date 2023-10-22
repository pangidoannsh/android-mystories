package com.pangidoannsh.mystories.widget

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.pangidoannsh.mystories.R
import com.pangidoannsh.mystories.data.api.response.StoryResponse

class StackRemoteViewsFactory(
    private val mContext: Context,
    private val listStories: List<StoryResponse>?
) : RemoteViewsService.RemoteViewsFactory {

    override fun onCreate() {

    }

    override fun onDataSetChanged() {
//        listStories.add(BitmapFactory.decodeResource(mContext.resources, R.drawable.logo))
//        listStories.add(BitmapFactory.decodeResource(mContext.resources, R.drawable.monocrom_logo))
    }

    override fun onDestroy() {

    }

    override fun getCount(): Int = listStories?.size ?: 0

    override fun getViewAt(position: Int): RemoteViews {
        val views = RemoteViews(mContext.packageName, R.layout.widget_item)
        listStories?.let {
            val bitmap = Glide.with(mContext)
                .asBitmap()
                .load("https://images.unsplash.com/photo-1529665253569-6d01c0eaf7b6?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1985&q=80")
                .submit()
                .get()
            views.setImageViewBitmap(R.id.imageView, bitmap)
        }

        val extras = bundleOf(
            StoriesBannerWidget.EXTRA_ITEM to position
        )
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)
        views.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        return views
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(position: Int): Long = 0

    override fun hasStableIds(): Boolean = false
}