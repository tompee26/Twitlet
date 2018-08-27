package com.tompee.twitlet.feature.login.page

import android.app.DialogFragment
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.TextView
import com.tompee.twitlet.R
import com.tompee.twitlet.base.BaseDialogFragment

class ProgressDialog : BaseDialogFragment() {
    companion object {
        private const val TAG_COLOR = "color"
        private const val TAG_TEXT = "text"

        fun newInstance(background: Int, textRes: Int): ProgressDialog {
            val dialog = ProgressDialog()
            dialog.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.FragmentDialog)
            val bundle = Bundle()
            bundle.putInt(TAG_COLOR, background)
            bundle.putInt(TAG_TEXT, textRes)
            dialog.arguments = bundle
            return dialog
        }
    }

    override fun setupComponent() {
    }

    override fun layoutId(): Int = R.layout.dialog_progress

    override fun setupView(view: View) {
        view.setBackgroundColor(ContextCompat.getColor(context!!,
                arguments?.getInt(TAG_COLOR) ?: R.color.colorLoginButton))
        val textView = view.findViewById<TextView>(R.id.progress_text)
        textView.setText(arguments?.getInt(TAG_TEXT) ?: R.string.progress_login_authenticate)
    }
}