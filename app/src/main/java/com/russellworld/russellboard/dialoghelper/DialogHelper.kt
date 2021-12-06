package com.russellworld.russellboard.dialoghelper

import android.app.AlertDialog
import android.view.View
import android.widget.Toast
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

        setDialogState(index, rootDialogElement)

        val dialog = builder.create()
        rootDialogElement.btnSignUpIn.setOnClickListener {
            setOnClickSignUpIn(dialog, index, rootDialogElement)
        }
        rootDialogElement.btnForgetPassword.setOnClickListener {
            setOnClickResetPassword(dialog, rootDialogElement)
        }
        dialog.show()
    }

    private fun setOnClickResetPassword(dialog: AlertDialog, rootDialogElement: SignDialogBinding) {
        if (rootDialogElement.edSignEmail.text.isNotEmpty()) {
            mainActivity.mAuth.sendPasswordResetEmail(rootDialogElement.edSignEmail.text.toString())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            mainActivity,
                            R.string.email_resset_password_sent,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            dialog.dismiss()
        } else {
            rootDialogElement.tvDialogMessage.visibility = View.VISIBLE
        }
    }


    private fun setOnClickSignUpIn(
        dialog: AlertDialog,
        index: Int,
        rootDialogElement: SignDialogBinding
    ) {
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

    private fun setDialogState(
        index: Int,
        rootDialogElement: SignDialogBinding
    ) {
        if (index == SIGN_UP_STATE) {
            rootDialogElement.tvTitleSign.text = mainActivity.resources.getString(R.string.acc_registration)
            rootDialogElement.btnSignUpIn.text = mainActivity.resources.getString(R.string.sign_up_action)
        } else {
            rootDialogElement.tvTitleSign.text = mainActivity.resources.getString(R.string.acc_sign_in)
            rootDialogElement.btnSignUpIn.text = mainActivity.resources.getString(R.string.sign_in_action)
            rootDialogElement.btnForgetPassword.visibility = View.VISIBLE
        }
    }
}