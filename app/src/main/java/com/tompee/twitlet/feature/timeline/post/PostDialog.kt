package com.tompee.twitlet.feature.timeline.post

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import com.jakewharton.rxbinding2.widget.RxTextView
import com.squareup.picasso.Picasso
import com.tompee.twitlet.R
import com.tompee.twitlet.TwitletApplication
import com.tompee.twitlet.base.BaseDialogFragment
import com.tompee.twitlet.dependency.component.DaggerTimelineComponent
import com.tompee.twitlet.dependency.module.TimelineModule
import com.tompee.twitlet.model.User
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.dialog_post.view.*
import javax.inject.Inject

class PostDialog : BaseDialogFragment(), PostView {

    @Inject
    lateinit var postPresenter: PostPresenter
    private lateinit var customView: View
    private val post = PublishSubject.create<Any>()

    companion object {
        fun newInstance(): PostDialog {
            return PostDialog()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        customView = activity?.layoutInflater?.inflate(R.layout.dialog_post, null)!!
        postPresenter.attachView(this)
        return AlertDialog.Builder(activity!!)
                .setView(customView)
                .setCancelable(false)
                .setPositiveButton(R.string.label_post_button) { _, _ -> }
                .setNegativeButton(R.string.label_cancel_button) { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
    }

    override fun onResume() {
        super.onResume()
        val button = (dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE)
        button.setOnClickListener {
            post.onNext(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        postPresenter.detachView()
    }

    override fun setupComponent() {
        DaggerTimelineComponent.builder()
                .appComponent(TwitletApplication[context!!].component)
                .timelineModule(TimelineModule())
                .build()
                .inject(this)
    }

    //region PostView
    override fun post(): Observable<Any> = post

    override fun message(): Observable<String> = RxTextView.textChanges(customView.postField)
            .map { it.toString() }

    override fun setUser(user: User) {
        if (user.imageUrl.isNotEmpty()) {
            Picasso.get()
                    .load(user.imageUrl)
                    .into(customView.profileImage)
        }
        customView.name.text = user.nickname
        customView.email.text = user.email
    }

    //endregion
}