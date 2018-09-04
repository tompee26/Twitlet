package com.tompee.twitlet.feature.timeline.delete

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import com.tompee.twitlet.R
import com.tompee.twitlet.TwitletApplication
import com.tompee.twitlet.base.BaseDialogFragment
import com.tompee.twitlet.dependency.component.DaggerTimelineComponent
import com.tompee.twitlet.dependency.module.TimelineModule
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class DeleteDialog : BaseDialogFragment(), DeleteView {

    @Inject
    lateinit var deletePresenter: DeletePresenter
    private val deleteSubject = PublishSubject.create<Any>()

    companion object {
        private const val TAG_POST_ID = "postId"
        private const val TAG_IMAGE_URL = "imageUrl"

        fun newInstance(postId: String, imageUrl: String): DeleteDialog {
            val dialog = DeleteDialog()
            val bundle = Bundle()
            bundle.putString(TAG_POST_ID, postId)
            bundle.putString(TAG_IMAGE_URL, imageUrl)
            dialog.arguments = bundle
            return dialog
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        deletePresenter.attachView(this)
        return AlertDialog.Builder(activity!!)
                .setTitle(R.string.label_delete)
                .setMessage(R.string.message_delete)
                .setCancelable(false)
                .setPositiveButton(R.string.label_delete_button) { _, _ -> }
                .setNegativeButton(R.string.label_cancel_button) { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
    }

    override fun onResume() {
        super.onResume()
        val button = (dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE)
        button.setOnClickListener {
            deleteSubject.onNext(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        deletePresenter.detachView()
    }

    override fun setupComponent() {
        DaggerTimelineComponent.builder()
                .appComponent(TwitletApplication[context!!].component)
                .timelineModule(TimelineModule(activity?.supportFragmentManager!!))
                .build()
                .inject(this)
    }

    //region DeleteView
    override fun delete(): Observable<Any> = deleteSubject

    override fun postId(): String = arguments?.getString(TAG_POST_ID)!!

    override fun imageUrl(): String = arguments?.getString(TAG_IMAGE_URL)!!

    //endregion
}