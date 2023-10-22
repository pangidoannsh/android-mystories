package com.pangidoannsh.mystories.view.story

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pangidoannsh.mystories.data.StoryInterface
import com.pangidoannsh.mystories.databinding.AdapterStoryBinding
import com.pangidoannsh.mystories.view.story.detailstory.DetailStoryActivity

class StoryAdapter(
    private var listStories: List<StoryInterface>
) : RecyclerView.Adapter<StoryAdapter.ViewHolder>() {
    class ViewHolder(var binding: AdapterStoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryAdapter.ViewHolder {
        val binding =
            AdapterStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryAdapter.ViewHolder, position: Int) {
        val story = listStories[position]


        holder.apply {
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
                        holder.itemView.context as Activity,
                        Pair(holder.binding?.storyImage, "image"),
                        Pair(holder.binding?.storyDescription, "description"),
                        Pair(holder.binding?.storyName, "name"),
                    )
                holder.itemView.context.startActivity(toDetail, optionsCompat.toBundle())
            }
        }
    }

    override fun getItemCount(): Int = listStories.size

}