package com.tompee.twitlet.feature.post

import android.os.Bundle
import android.view.View
import com.tompee.twitlet.Constants
import com.tompee.twitlet.R
import com.tompee.twitlet.TwitletApplication
import com.tompee.twitlet.base.BaseActivity
import com.tompee.twitlet.core.image.Renderer
import com.tompee.twitlet.dependency.component.DaggerPostComponent
import com.tompee.twitlet.dependency.module.PostModule
import com.tompee.twitlet.model.Message
import com.tompee.twitlet.model.Post
import com.tompee.twitlet.model.User
import kotlinx.android.synthetic.main.activity_post.*
import kotlinx.android.synthetic.main.toolbar.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class PostActivity : BaseActivity(), PostView {

    companion object {
        const val TAG_POST_ID = "postId"
        const val TAG_PROFILE_IMAGE = "profileImage"
        const val TAG_PROFILE_NAME = "profileName"
        const val TAG_PROFILE_EMAIL = "profileEmail"
        const val TAG_POST_MESSAGE = "postMessage"
        const val TAG_POST_IMAGE = "postImage"
        const val TAG_POST_TIME = "postTime"
    }

    @Inject
    lateinit var postPresenter: PostPresenter

    @Inject
    lateinit var renderer: Renderer

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
        val format = SimpleDateFormat(Constants.DATE_TIME_FORMAT, Locale.getDefault())
        val message = Message(intent.getStringExtra(TAG_POST_ID),
                intent.getStringExtra(TAG_POST_MESSAGE),
                intent.getStringExtra(TAG_POST_IMAGE),
                format.parse(intent.getStringExtra(TAG_POST_TIME)))

        val user = User(intent.getStringExtra(TAG_PROFILE_EMAIL), false,
                intent.getStringExtra(TAG_PROFILE_NAME),
                intent.getStringExtra(TAG_PROFILE_IMAGE))

        DaggerPostComponent.builder()
                .appComponent(TwitletApplication[this].component)
                .postModule(PostModule(Post(message, user)))
                .build()
                .inject(this)
    }

    //region PostView
    override fun postId(): String = intent.getStringExtra(TAG_POST_ID)

    override fun setPost(post: Post) {
        /* profile image */
        if (post.user.imageUrl.isNotEmpty()) {
            renderer.displayImage(post.user.imageUrl, profileImage)
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
            renderer.displayImage(post.message.image, postPhoto)
        }

        val format = SimpleDateFormat(Constants.DATE_READABLE_FORMAT, Locale.getDefault())
        time.text = format.format(post.message.time)
    }
    //endregion
}