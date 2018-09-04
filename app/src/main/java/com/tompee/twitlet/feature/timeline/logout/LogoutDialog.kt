package com.tompee.twitlet.feature.timeline.logout

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.tompee.twitlet.R
import com.tompee.twitlet.TwitletApplication
import com.tompee.twitlet.base.BaseDialogFragment
import com.tompee.twitlet.dependency.component.DaggerTimelineComponent
import com.tompee.twitlet.dependency.module.TimelineModule
import com.tompee.twitlet.feature.login.LoginActivity
import com.tompee.twitlet.feature.timeline.post.PostView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class LogoutDialog : BaseDialogFragment(), LogoutView {

    @Inject
    lateinit var logoutPresenter: LogoutPresenter
    private val logoutSubject = PublishSubject.create<Any>()

    companion object {
        fun newInstance(): LogoutDialog {
            return LogoutDialog()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        logoutPresenter.attachView(this)
        return AlertDialog.Builder(activity!!)
                .setTitle(R.string.label_logout)
                .setMessage(R.string.message_logout)
                .setCancelable(false)
                .setPositiveButton(R.string.label_logout_button) { _, _ -> }
                .setNegativeButton(R.string.label_cancel_button) { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
    }

    override fun onResume() {
        super.onResume()
        val button = (dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE)
        button.setOnClickListener {
            logoutSubject.onNext(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        logoutPresenter.detachView()
    }

    override fun setupComponent() {
        DaggerTimelineComponent.builder()
                .appComponent(TwitletApplication[context!!].component)
                .timelineModule(TimelineModule(activity!!))
                .build()
                .inject(this)
    }

    //region PostView
    override fun logout(): Observable<Any> = logoutSubject

    override fun moveToLoginScreen() {
        val intent = Intent(activity, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        activity?.startActivity(intent)
        activity?.finish()
    }

    //endregion
}