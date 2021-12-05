package com.russellworld.russellboard.dialoghelper

import android.app.AlertDialog
import com.russellworld.russellboard.MainActivity
import com.russellworld.russellboard.R
import com.russellworld.russellboard.accounthelper.AccountHelper
import com.russellworld.russellboard.databinding.SignDialogBinding
import com.russellworld.russellboard.utilits.SIGN_UP_STATE

class DialogHelper(private val mainActivity: MainActivity) {
    private val accountHelper = AccountHelper(mainActivity)

    fun createSignDialog(index: Int) {
        val builder = AlertDialog.Builder(mainActivity)
        val rootDialogElement = SignDialogBinding.inflate(mainActivity.layoutInflater)
        builder.setView(rootDialogElement.root)


        if (index == SIGN_UP_STATE) {
            rootDialogElement.tvTitleSign.text = mainActivity.resources.getString(R.string.acc_registration)
            rootDialogElement.btnSignUp.text = mainActivity.resources.getString(R.string.sign_up_action)
        } else {
            rootDialogElement.tvTitleSign.text = mainActivity.resources.getString(R.string.acc_sign_in)
            rootDialogElement.btnSignUp.text = mainActivity.resources.getString(R.string.sign_in_action)
        }
        val dialog = builder.create()
        rootDialogElement.btnSignUp.setOnClickListener {
            dialog.dismiss()
            if (index == SIGN_UP_STATE) {
                accountHelper.signUpWithEmail(
                    rootDialogElement.edSignEmail.text.toString(),
                    rootDialogElement.edSignPassword.text.toString()
                )
            } else {
                accountHelper.signInWithEmail(
                    rootDialogElement.edSignEmail.text.toString(),
                    rootDialogElement.edSignPassword.text.toString()
                )
            }
        }
        dialog.show()
    }
}