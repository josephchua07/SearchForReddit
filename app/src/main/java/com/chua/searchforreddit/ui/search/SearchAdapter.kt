package com.chua.searchforreddit.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chua.searchforreddit.databinding.PostItemBinding
import com.chua.searchforreddit.domain.Post

class SearchAdapter(private val navigate: (String) -> Unit) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    private var dataSet: MutableList<Post> = mutableListOf()

    fun updateDataSet(dataSet: List<Post>) {
        this.dataSet = dataSet.toMutableList()
    }

    class ViewHolder(private val binding: PostItemBinding, val navigate: (String) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(post: Post) {
            binding.apply {
                title.text = "Title: ${post.title}"
                upVotes.text = "Ups: ${post.likes}"
                comments.text = "Number of comments: ${post.comments}"
                root.setOnClickListener {
                    navigate(post.url.drop(1))
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PostItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, navigate)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataSet[position])
    }

    override fun getItemCount() = dataSet.size

}