package com.tompee.twitlet.feature.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.tompee.twitlet.Constants
import com.tompee.twitlet.R
import com.tompee.twitlet.TwitletApplication
import com.tompee.twitlet.base.BaseActivity
import com.tompee.twitlet.dependency.component.DaggerProfileComponent
import com.tompee.twitlet.dependency.module.ProfileModule
import com.tompee.twitlet.feature.common.ProgressDialog
import com.tompee.twitlet.feature.timeline.TimelineActivity
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.activity_profile_edit.*
import javax.inject.Inject

class ProfileActivity : BaseActivity(), ProfileView {

    @Inject
    lateinit var profilePresenter: ProfilePresenter

    private val imageSubject = BehaviorSubject.create<Uri>()

    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressDialog = ProgressDialog.newInstance(R.color.colorPrimary, R.string.profile_save_progress)
        profilePresenter.attachView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        profilePresenter.detachView()
    }

    override fun layoutId() = R.layout.activity_profile_edit

    override fun setupComponent() {
        DaggerProfileComponent.builder()
                .appComponent(TwitletApplication[this].component)
                .profileModule(ProfileModule())
                .build()
                .inject(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri = result.uri
                circularImageView.setImageURI(resultUri)
                imageSubject.onNext(resultUri)
            }
        }
    }

    //region ProfileView
    override fun profileImageRequest(): Observable<Any> = RxView.clicks(circularImageView)

    override fun photoUrl(): Observable<String> = imageSubject
            .map { it.toString() }

    override fun nickname(): Observable<String> = RxTextView.textChanges(nickname)
            .skipInitialValue()
            .map { it.toString() }
            .map { it.trim() }

    override fun saveProfile(): Observable<Any> = RxView.clicks(saveButton)

    override fun showProgress() {
        progressDialog.show(supportFragmentManager, "dialog")
    }

    override fun dismissProgress() {
        progressDialog.dismiss()
    }

    override fun startImageCropper() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setCropShape(CropImageView.CropShape.OVAL)
                .setMinCropResultSize(Constants.IMAGE_SIZE, Constants.IMAGE_SIZE)
                .setRequestedSize(Constants.IMAGE_SIZE, Constants.IMAGE_SIZE,
                        CropImageView.RequestSizeOptions.RESIZE_INSIDE)
                .setAspectRatio(1, 1)
                .setActivityMenuIconColor(ContextCompat.getColor(this, R.color.colorLight))
                .setAllowFlipping(false)
                .setActivityTitle(getString(R.string.profile_label_picture))
                .start(this)
    }

    override fun showEmptyNameError() {
        nickname.error = getString(R.string.error_field_required)
        nickname.requestFocus()
    }

    override fun moveToTimelineScreen() {
        val intent = Intent(this, TimelineActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

    override fun showError(message: String) {
        Snackbar.make(findViewById(android.R.id.content)!!, message, Snackbar.LENGTH_LONG).show()
    }

    //endregion
}