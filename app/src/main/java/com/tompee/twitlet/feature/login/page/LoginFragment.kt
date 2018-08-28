package com.tompee.twitlet.feature.login.page

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.tompee.twitlet.R
import com.tompee.twitlet.base.BaseFragment
import com.tompee.twitlet.feature.common.ProgressDialog
import com.tompee.twitlet.feature.login.LoginActivity
import com.tompee.twitlet.feature.profile.ProfileActivity
import com.tompee.twitlet.feature.timeline.TimelineActivity
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_login.*
import javax.inject.Inject

class LoginFragment : BaseFragment(), LoginPageView {

    @Inject
    lateinit var loginPagePresenter: LoginPagePresenter

    private lateinit var listener: PageSwitchListener
    private lateinit var progressDialog: ProgressDialog

    companion object {
        const val LOGIN = 0
        const val SIGN_UP = 1
        private const val TYPE_TAG = "type"

        fun newInstance(type: Int): LoginFragment {
            val loginFragment = LoginFragment()
            val bundle = Bundle()
            bundle.putInt(TYPE_TAG, type)
            loginFragment.arguments = bundle
            return loginFragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginPagePresenter.attachView(this)
        val type = arguments?.getInt(TYPE_TAG) ?: LOGIN
        switchButton.setOnClickListener { listener.onPageSwitch(type) }
        if (type == LOGIN) {
            progressDialog = ProgressDialog.newInstance(R.color.colorLoginButton, R.string.progress_login_authenticate)
            switchButton.text = getString(R.string.label_login_new_account)
            commandButton.text = getString(R.string.label_login_button)
            commandButton.setBackgroundResource(R.drawable.ripple_login)
        } else {
            progressDialog = ProgressDialog.newInstance(R.color.colorSignupButton, R.string.progress_login_register)
            switchButton.text = getString(R.string.label_login_registered)
            commandButton.text = getString(R.string.label_login_sign_up)
            commandButton.setBackgroundResource(R.drawable.ripple_sign_up)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is PageSwitchListener) {
            listener = context
        }
    }

    override fun layoutId() = R.layout.fragment_login

    override fun setupComponent() {
        (activity as LoginActivity).loginComponent.inject(this)
    }

    //region LoginPageView
    override fun getEmail(): Observable<String> = RxTextView.textChanges(userView)
            .skipInitialValue()
            .distinct()
            .map { it.toString() }
            .map { it.trim() }

    override fun getPassword(): Observable<String> = RxTextView.textChanges(passView)
            .skipInitialValue()
            .distinct()
            .map { it.toString() }
            .map { it.trim() }

    override fun getViewType(): Int = arguments?.getInt(TYPE_TAG) ?: LoginFragment.LOGIN

    override fun command(): Observable<Any> = RxView.clicks(commandButton)
            .doOnNext { hideKeyboard() }

    override fun showEmptyEmailError() {
        userView.error = getString(R.string.error_field_required)
        userView.requestFocus()
    }

    override fun showInvalidEmailError() {
        userView.error = getString(R.string.error_invalid_email)
        userView.requestFocus()
    }

    override fun clearEmailError() {
        userView.error = null
    }

    override fun showEmptyPasswordError() {
        passView.error = getString(R.string.error_field_required)
        passView.requestFocus()
    }

    override fun showPasswordShortError() {
        passView.error = getString(R.string.error_pass_min)
        passView.requestFocus()
    }

    override fun clearPasswordError() {
        passView.error = null
    }

    override fun showProgressDialog() {
        progressDialog.show(fragmentManager, "progress")
    }

    override fun dismissProgressDialog() {
        progressDialog.dismiss()
    }

    override fun showSignupSuccessMessage() {
        AlertDialog.Builder(activity!!, R.style.DialogStyle)
                .setTitle(R.string.signup_successful_title)
                .setMessage(R.string.signup_verify_rationale)
                .setPositiveButton(R.string.label_positive_button, null)
                .show()
    }

    override fun showError(message: String) {
        Snackbar.make(activity?.findViewById(android.R.id.content)!!,
                message, Snackbar.LENGTH_LONG).show()
    }

    override fun moveToProfileScreen() {
        val intent = Intent(context, ProfileActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        activity?.finish()
    }

    override fun moveToTimelineScreen() {
        val intent = Intent(context, TimelineActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        activity?.finish()
    }

    //endregion

    private fun hideKeyboard() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}