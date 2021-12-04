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
        val _binding = SignDialogBinding.inflate(mainActivity.layoutInflater)

        if (index == SIGN_UP_STATE) {
            _binding.tvTitleSign.text = mainActivity.resources.getString(R.string.acc_registration)
            _binding.btnSignUp.text = mainActivity.resources.getString(R.string.sign_up_action)
        } else {
            _binding.tvTitleSign.text = mainActivity.resources.getString(R.string.acc_sign_in)
            _binding.btnSignUp.text = mainActivity.resources.getString(R.string.sign_in_action)
        }
        _binding.btnSignIn.setOnClickListener {
            if (index == SIGN_UP_STATE) {
                accountHelper.signUpWithEmail(
                    _binding.edSignEmail.text.toString(),
                    _binding.edSignPassword.text.toString()
                )
            } else {

            }
        }
        builder.setView(_binding.root)
        builder.show()
    }
}