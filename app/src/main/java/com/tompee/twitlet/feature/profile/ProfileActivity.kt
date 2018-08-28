package com.tompee.twitlet.feature.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import com.jakewharton.rxbinding2.view.RxView
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.tompee.twitlet.Constants
import com.tompee.twitlet.R
import com.tompee.twitlet.TwitletApplication
import com.tompee.twitlet.base.BaseActivity
import com.tompee.twitlet.dependency.component.DaggerProfileComponent
import com.tompee.twitlet.dependency.module.ProfileModule
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_profile_edit.*
import javax.inject.Inject

class ProfileActivity : BaseActivity(), ProfileView {

    @Inject
    lateinit var profilePresenter: ProfilePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            }
        }
    }

    //region ProfileView
    override fun profileImageRequest(): Observable<Any> = RxView.clicks(circularImageView)

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

    //endregion
}