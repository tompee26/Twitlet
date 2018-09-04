package com.tompee.twitlet.feature.timeline.post

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import com.jakewharton.rxbinding2.widget.RxTextView
import com.squareup.picasso.Picasso
import com.tompee.twitlet.R
import com.tompee.twitlet.TwitletApplication
import com.tompee.twitlet.base.BaseDialogFragment
import com.tompee.twitlet.dependency.component.DaggerTimelineComponent
import com.tompee.twitlet.dependency.module.TimelineModule
import com.tompee.twitlet.feature.common.ProgressDialog
import com.tompee.twitlet.model.User
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.dialog_post.view.*
import java.io.File
import javax.inject.Inject


class PostDialog : BaseDialogFragment(), PostView {

    @Inject
    lateinit var postPresenter: PostPresenter
    private lateinit var customView: View
    private val post = PublishSubject.create<Any>()
    private val image = PublishSubject.create<String>()
    private val progressDialog = ProgressDialog.newInstance(R.color.colorPrimary, R.string.message_post_progress)

    companion object {
        private const val RESULT_LOAD_IMAGE = 1

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
                .setNeutralButton(R.string.label_post_add_photo) { _, _ -> }
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
        val neutralButton = (dialog as AlertDialog).getButton(Dialog.BUTTON_NEUTRAL)
        neutralButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, RESULT_LOAD_IMAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            val selectedImage = data.data
            val columns = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = activity?.contentResolver?.query(selectedImage, columns,
                    null, null, null)
            cursor?.apply {
                moveToFirst()
                val index = getColumnIndex(columns[0])
                val path = getString(index)
                cursor.close()
                image.onNext(path)
                Picasso.get()
                        .load(File(path))
                        .into(customView.postPhoto)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        postPresenter.detachView()
    }

    override fun setupComponent() {
        DaggerTimelineComponent.builder()
                .appComponent(TwitletApplication[context!!].component)
                .timelineModule(TimelineModule(activity!!))
                .build()
                .inject(this)
    }

    //region PostView
    override fun image(): Observable<String> = image

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

    override fun showProgressDialog() {
        progressDialog.show(activity?.supportFragmentManager, "progressDialog")
    }

    override fun dismissProgressDialog() {
        progressDialog.dismiss()
    }
    //endregion
}