package com.romeo.eatmeapp.ui.dialogs

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.romeo.eatmeapp.R

object InfoDialog {
    private var currentDialog: AlertDialog? = null
    private var isDialogShowing = false

    fun show(fragment: Fragment, message: String) {
        val view = LayoutInflater.from(fragment.requireContext())
            .inflate(R.layout.dialog_info, null)

        val messageTextView = view.findViewById<TextView>(R.id.tvDialogMessage)
        val okButton = view.findViewById<Button>(R.id.btn_ok)

        messageTextView.text = message
        okButton.text = fragment.getString(R.string.dialog_btn_text)

        val dialog = AlertDialog.Builder(fragment.requireContext())
            .setView(view)
            .setCancelable(true)
            .create()

        dialog.setOnShowListener {
            val window = dialog.window
            window?.setLayout(
                (fragment.resources.displayMetrics.widthPixels * 0.85).toInt(),
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            isDialogShowing = true
        }

        okButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.setOnDismissListener {
            currentDialog = null
            isDialogShowing = false
        }

        currentDialog = dialog
        dialog.show()
    }

    fun show(fragment: Fragment, messageId: Int) {
        show(fragment, fragment.getString(messageId))
    }

    fun dismiss() {
        try {
            currentDialog?.dismiss()
        } catch (e: Exception) {
           Log.e("InfoDialog", "Dismiss error: $e")
        }
        currentDialog = null
        isDialogShowing = false
    }

    fun isShowing(): Boolean {
        return isDialogShowing && currentDialog?.isShowing == true
    }
}