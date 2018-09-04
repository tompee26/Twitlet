package com.tompee.twitlet.feature.post

import android.os.Bundle
import android.view.View
import com.squareup.picasso.Picasso
import com.tompee.twitlet.Constants
import com.tompee.twitlet.R
import com.tompee.twitlet.TwitletApplication
import com.tompee.twitlet.base.BaseActivity
import com.tompee.twitlet.dependency.component.DaggerPostComponent
import com.tompee.twitlet.dependency.module.PostModule
import com.tompee.twitlet.model.Post
import kotlinx.android.synthetic.main.activity_post.*
import kotlinx.android.synthetic.main.toolbar.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class PostActivity : BaseActivity(), PostView {

    companion object {
        const val TAG_POST_ID = "postId"
    }

    @Inject
    lateinit var postPresenter: PostPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }
        toolbar_text.setText(R.string.label_post_detail)
        postPresenter.attachView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        postPresenter.detachView()
    }

    override fun layoutId(): Int = R.layout.activity_post

    override fun setupComponent() {
        DaggerPostComponent.builder()
                .appComponent(TwitletApplication[this].component)
                .postModule(PostModule())
                .build()
                .inject(this)
    }

    //region PostView
    override fun postId(): String = intent.getStringExtra(TAG_POST_ID)

    override fun setPost(post: Post) {
        /* profile image */
        if (post.user.imageUrl.isNotEmpty()) {
            Picasso.get()
                    .load(post.user.imageUrl)
                    .into(profileImage)
        }

        /* name */
        name.text = post.user.nickname

        /* email */
        email.text = post.user.email

        /* message */
        if (post.message.message.isNotEmpty()) {
            postField.text = post.message.message
        } else {
            postField.visibility = View.GONE
        }

        /* photo */
        if (post.message.image.isNotEmpty()) {
            Picasso.get()
                    .load(post.message.image)
                    .into(postPhoto)
        }

        val format = SimpleDateFormat(Constants.DATE_READABLE_FORMAT, Locale.getDefault())
        time.text = format.format(post.message.time)
    }
    //endregion
}