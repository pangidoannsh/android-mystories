package com.pangidoannsh.mystories.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pangidoannsh.mystories.data.api.response.StoryResponse
import com.pangidoannsh.mystories.databinding.AdapterStoryBinding
import com.pangidoannsh.mystories.view.story.detailstory.DetailStoryActivity

class StoryAdapter :
    PagingDataAdapter<StoryResponse, StoryAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            AdapterStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    class ViewHolder(private val binding: AdapterStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: StoryResponse) {
            binding.storyName.text = story.name
            binding.storyDescription.text = story.description

            if (story.photoUrl !== null) {
                Glide.with(itemView.context)
                    .load(story.photoUrl)
                    .into(binding.storyImage)
            }

            itemView.setOnClickListener {
                val toDetail = Intent(itemView.context, DetailStoryActivity::class.java)

                toDetail.apply {
                    putExtra(DetailStoryActivity.EXTRA_ID, story.id)
                    putExtra(DetailStoryActivity.EXTRA_NAME, story.name)
                    putExtra(DetailStoryActivity.EXTRA_DESCRIPTION, story.description)
                    putExtra(DetailStoryActivity.EXTRA_PHOTO_URL, story.photoUrl)
                }


                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(binding?.storyImage, "image"),
                        Pair(binding?.storyDescription, "description"),
                        Pair(binding?.storyName, "name"),
                    )
                itemView.context.startActivity(toDetail, optionsCompat.toBundle())
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryResponse>() {
            override fun areItemsTheSame(oldItem: StoryResponse, newItem: StoryResponse): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: StoryResponse,
                newItem: StoryResponse
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

}