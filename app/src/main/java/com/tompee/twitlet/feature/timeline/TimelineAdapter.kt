package com.tompee.twitlet.feature.timeline

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tompee.twitlet.R
import com.tompee.twitlet.model.Post
import kotlinx.android.synthetic.main.list_post.view.*
import java.text.SimpleDateFormat
import java.util.*

class TimelineAdapter(private val postList: List<Post>) : RecyclerView.Adapter<TimelineAdapter.TimelineViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimelineViewHolder =
            TimelineViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_post, parent, false))

    override fun getItemCount(): Int = postList.count()

    override fun onBindViewHolder(holder: TimelineViewHolder, position: Int) =
            holder.bind(postList[position])

    inner class TimelineViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(post: Post) {
            val format = SimpleDateFormat("MMM dd hh:mmaa", Locale.getDefault())
            view.name.text = post.user.nickname
            view.time.text = format.format(post.message.time)
            view.messageBox.text = post.message.message
        }
    }
}