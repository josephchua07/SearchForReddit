package com.chua.searchforreddit.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chua.searchforreddit.databinding.PostItemBinding
import com.chua.searchforreddit.domain.model.Post

class SearchAdapter : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    private var dataSet: MutableList<Post> = mutableListOf()

    fun updateDataSet(dataSet: List<Post>) {
        this.dataSet = dataSet.toMutableList()
    }

    class ViewHolder(val binding: PostItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(post: Post) {
            binding.title.text = "Title: ${post.title}"
            binding.upVotes.text = "UpVotes: ${post.ups}"
            binding.comments.text = "Number of comments: ${post.num_comments}"
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PostItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataSet[position])
    }

    override fun getItemCount() = dataSet.size

}