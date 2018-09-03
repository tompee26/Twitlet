package com.tompee.twitlet.feature.timeline

import android.support.v4.app.FragmentManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import com.tompee.twitlet.R
import com.tompee.twitlet.feature.timeline.delete.DeleteDialog
import com.tompee.twitlet.model.Post
import kotlinx.android.synthetic.main.list_post.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TimelineAdapter(private val supportFragmentManager: FragmentManager) : RecyclerView.Adapter<TimelineAdapter.TimelineViewHolder>() {
    var postList: List<Post> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimelineViewHolder =
            TimelineViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_post, parent, false))

    override fun getItemCount(): Int = postList.count()

    override fun onBindViewHolder(holder: TimelineViewHolder, position: Int) =
            holder.bind(postList[position])

    inner class TimelineViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(post: Post) {
            val format = SimpleDateFormat("MMM d 'at' h:mmaa", Locale.getDefault())
            view.name.text = post.user.nickname
            view.time.text = format.format(post.message.time)
            view.messageBox.text = post.message.message

            if (post.user.imageUrl.isNotEmpty()) {
                Picasso.get()
                        .load(post.user.imageUrl)
                        .into(view.profileImage)
            } else {
                view.profileImage.setImageResource(R.drawable.ic_account_circle_primary)
            }

            if (post.user.isAuthenticated) {
                view.deletePost.visibility = View.VISIBLE
            } else {
                view.deletePost.visibility = View.INVISIBLE
            }

            view.deletePost.setOnClickListener {
                val dialog = DeleteDialog.newInstance(post.message.postId)
                dialog.show(supportFragmentManager, "deleteDialog")
            }
        }
    }
}