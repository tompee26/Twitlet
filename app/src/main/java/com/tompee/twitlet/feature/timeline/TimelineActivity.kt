package com.tompee.twitlet.feature.timeline

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.tompee.twitlet.R
import com.tompee.twitlet.TwitletApplication
import com.tompee.twitlet.base.BaseActivity
import com.tompee.twitlet.dependency.component.DaggerTimelineComponent
import com.tompee.twitlet.dependency.module.TimelineModule
import com.tompee.twitlet.feature.about.AboutActivity
import com.tompee.twitlet.feature.license.LicenseActivity
import com.tompee.twitlet.feature.timeline.post.PostDialog
import kotlinx.android.synthetic.main.activity_timeline.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class TimelineActivity : BaseActivity(), TimelineView {

    @Inject
    lateinit var timelinePresenter: TimelinePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowTitleEnabled(false)
        }
        toolbar_text.setText(R.string.label_timeline)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
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
            R.id.post -> {
                val postDialog = PostDialog.newInstance()
                postDialog.show(supportFragmentManager, "postDialog")
            }
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
    //endregion
}