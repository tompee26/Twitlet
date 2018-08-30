package com.tompee.twitlet.feature.timeline

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tompee.twitlet.R
import com.tompee.twitlet.model.Post
import com.tompee.twitlet.model.User
import kotlinx.android.synthetic.main.list_post.view.*

class TimelineAdapter(private val postList: List<Post>,
                      private val user: User) : RecyclerView.Adapter<TimelineAdapter.TimelineViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimelineViewHolder =
            TimelineViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_post, parent, false))

    override fun getItemCount(): Int = postList.count()

    override fun onBindViewHolder(holder: TimelineViewHolder, position: Int) =
            holder.bind(postList[position])

    inner class TimelineViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(post: Post) {
            view.profileImage.setImageBitmap(user.bitmap)
            view.name.text = user.nickname
            view.email.text = user.email
        }
    }
}