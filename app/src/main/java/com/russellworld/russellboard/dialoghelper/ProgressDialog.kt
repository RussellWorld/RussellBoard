package com.russellworld.russellboard.dialoghelper

import android.app.Activity
import android.app.AlertDialog
import com.russellworld.russellboard.databinding.ProgressDialogLayoutBinding

object ProgressDialog {

    fun createProgressDialog(activity: Activity): AlertDialog {
        val builder = AlertDialog.Builder(activity)
        val rootDialogElement = ProgressDialogLayoutBinding.inflate(activity.layoutInflater)
        builder.setView(rootDialogElement.root)
        val dialog = builder.create()

        dialog.setCancelable(false)
        dialog.show()
        return dialog
    }
}