package com.russellworld.russellboard.accounthelper

import android.widget.Toast
import com.google.firebase.auth.FirebaseUser
import com.russellworld.russellboard.MainActivity
import com.russellworld.russellboard.R

class AccountHelper(private val mainActivity: MainActivity) {
    fun signUpWithEmail(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            mainActivity.mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful){
                    sendEmailVerification(task.result.user!!)
                    mainActivity.uiUpdate(task.result?.user)
                } else {
                    Toast.makeText(
                        mainActivity,
                        mainActivity.getString(R.string.sign_up_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    fun signInWithEmail(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            mainActivity.mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful){
                    mainActivity.uiUpdate(task.result?.user)
                } else {
                    Toast.makeText(
                        mainActivity,
                        mainActivity.getString(R.string.sign_in_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun sendEmailVerification(user: FirebaseUser) {
        user.sendEmailVerification().addOnCompleteListener {task ->
            if (task.isSuccessful){
                Toast.makeText(
                    mainActivity,
                    mainActivity.getString(R.string.sign_verification_email_error),
                    Toast.LENGTH_SHORT
                ).show()
            }else {
                Toast.makeText(
                    mainActivity,
                    mainActivity.getString(R.string.sign_verification_done),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }
}