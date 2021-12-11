package com.russellworld.russellboard.accounthelper

import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.russellworld.russellboard.MainActivity
import com.russellworld.russellboard.R
import com.russellworld.russellboard.utilits.ERROR_EMAIL_ALREADY_IN_USE
import com.russellworld.russellboard.utilits.SIGN_IN_REQUEST_CODE

class AccountHelper(private val mainActivity: MainActivity) {
    private lateinit var signInClient: GoogleSignInClient


    fun signUpWithEmail(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            mainActivity.mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    sendEmailVerification(task.result.user!!)
                    mainActivity.uiUpdate(task.result?.user)
                } else {
                    if ((task.exception as FirebaseAuthUserCollisionException).errorCode == ERROR_EMAIL_ALREADY_IN_USE) {
                        linkEmailToG(email, password)
                    } else {
                        Toast.makeText(mainActivity, "${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }

                }
            }
        }
    }

    private fun linkEmailToG(email: String, password: String) {
        val credential = EmailAuthProvider.getCredential(email, password)

        if (mainActivity.mAuth.currentUser != null) {
            mainActivity.mAuth.currentUser!!.linkWithCredential(credential).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        mainActivity,
                        mainActivity.getString(R.string.link_done),
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            }
        } else {
            Toast.makeText(mainActivity, mainActivity.getString(R.string.enter_to_g), Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun getSignInClient(): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(mainActivity.getString(R.string.web_client_id)).requestEmail().build()
        return GoogleSignIn.getClient(mainActivity, gso)
    }

    fun signInWithGoogle() {
        signInClient = getSignInClient()
        val intent = signInClient.signInIntent
        mainActivity.startActivityForResult(intent, SIGN_IN_REQUEST_CODE)
    }

    fun signInFirebaseWithGoogle(token: String) {
        val credential = GoogleAuthProvider.getCredential(token, null)
        mainActivity.mAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(mainActivity, "Sign in done", Toast.LENGTH_SHORT).show()
                mainActivity.uiUpdate(task.result.user)
            }
        }
    }

    fun signInWithEmail(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            mainActivity.mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    mainActivity.uiUpdate(task.result?.user)
                } else {
                    Toast.makeText(mainActivity, "${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun sendEmailVerification(user: FirebaseUser) {
        user.sendEmailVerification().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(
                    mainActivity,
                    mainActivity.getString(R.string.sign_verification_email_error),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    mainActivity,
                    mainActivity.getString(R.string.sign_verification_done),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }
}