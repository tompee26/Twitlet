package com.tompee.twitlet.feature.login.page

import android.content.Context
import android.os.Bundle
import android.view.View
import com.jakewharton.rxbinding2.widget.RxTextView
import com.tompee.twitlet.R
import com.tompee.twitlet.base.BaseFragment
import com.tompee.twitlet.feature.login.LoginActivity
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_login.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class LoginFragment : BaseFragment(), LoginPageView {

    @Inject
    lateinit var loginPagePresenter: LoginPagePresenter

    private lateinit var listener: PageSwitchListener

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
//            progressDialog = ProgressDialog(context, R.style.AppTheme_Login_Dialog)
            switchButton.text = getString(R.string.label_login_new_account)
            commandButton.text = getString(R.string.label_login_button)
            commandButton.setBackgroundResource(R.drawable.ripple_login)
        } else {
//            progressDialog = ProgressDialog(context, R.style.AppTheme_SignUp_Dialog)
            switchButton.text = getString(R.string.label_login_registered)
            commandButton.text = getString(R.string.label_login_sign_up)
            commandButton.setBackgroundResource(R.drawable.ripple_sign_up)
        }
//        progressDialog.isIndeterminate = true
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
            .debounce(1, TimeUnit.SECONDS)
            .map { it.toString() }

    override fun getPassword(): Observable<String> = RxTextView.textChanges(passView)
            .skipInitialValue()
            .distinct()
            .debounce(1, TimeUnit.SECONDS)
            .map { it.toString() }

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

    //endregion
}