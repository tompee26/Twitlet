package com.tompee.twitlet.feature.timeline

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.jakewharton.rxbinding2.view.RxView
import com.squareup.picasso.Picasso
import com.tompee.twitlet.R
import com.tompee.twitlet.TwitletApplication
import com.tompee.twitlet.base.BaseActivity
import com.tompee.twitlet.dependency.component.DaggerTimelineComponent
import com.tompee.twitlet.dependency.module.TimelineModule
import com.tompee.twitlet.feature.about.AboutActivity
import com.tompee.twitlet.feature.license.LicenseActivity
import com.tompee.twitlet.feature.timeline.logout.LogoutDialog
import com.tompee.twitlet.feature.timeline.post.PostDialog
import com.tompee.twitlet.model.User
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_timeline.*
import kotlinx.android.synthetic.main.timeline_profile.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class TimelineActivity : BaseActivity(), TimelineView {

    @Inject
    lateinit var timelinePresenter: TimelinePresenter
    private val logoutSubject = PublishSubject.create<Any>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowTitleEnabled(false)
        }
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerDecorator(20))
        RxView.clicks(addPost)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    val postDialog = PostDialog.newInstance()
                    postDialog.show(supportFragmentManager, "postDialog")
                }
        logoutSubject.throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    val logoutDialog = LogoutDialog.newInstance()
                    logoutDialog.show(supportFragmentManager, "logoutDialog")
                }
        timelinePresenter.attachView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        timelinePresenter.detachView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.timeline, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> logoutSubject.onNext(Any())
            R.id.about -> {
                val intent = Intent(this, AboutActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }
            R.id.license -> {
                val intent = Intent(this, LicenseActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun layoutId(): Int = R.layout.activity_timeline

    override fun setupComponent() {
        DaggerTimelineComponent.builder()
                .appComponent(TwitletApplication[this].component)
                .timelineModule(TimelineModule())
                .build()
                .inject(this)
    }


    //region TimelineView
    override fun setAdapter(adapter: TimelineAdapter) {
        recyclerView.adapter = adapter
    }

    override fun setUser(user: User) {
        if (user.imageUrl.isNotEmpty()) {
            Picasso.get()
                    .load(user.imageUrl)
                    .into(profileImage)
        }
        name.text = user.nickname
        email.text = user.email
    }

    //endregion
}