package com.russellworld.russellboard.accounthelper

import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.*
import com.russellworld.russellboard.MainActivity
import com.russellworld.russellboard.R
import com.russellworld.russellboard.utilits.*

class AccountHelper(private val mainActivity: MainActivity) {
    private lateinit var signInClient: GoogleSignInClient


    fun signUpWithEmail(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            mainActivity.mAuth.currentUser?.delete()?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    mainActivity.mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task1 ->
                            if (task1.isSuccessful) {
                                signUpWithEmailSuccessful(task1.result.user!!)
                            } else {
                                signUpWithEmailException(task1.exception!!, email, password)
                            }
                        }
                }
            }
        }
    }

    private fun signUpWithEmailException(
        exc: Exception,
        email: String,
        password: String
    ) {
        when ((exc as FirebaseAuthUserCollisionException).errorCode) {
            ERROR_EMAIL_ALREADY_IN_USE -> {
                linkEmailToG(email, password)
            }
            else -> {
                Toast.makeText(mainActivity, "${exc.message}", Toast.LENGTH_LONG)
                    .show()
            }
        }
        when ((exc as FirebaseAuthInvalidCredentialsException).errorCode) {
            ERROR_INVALID_EMAIL -> {
                Toast.makeText(mainActivity, "${exc.message}", Toast.LENGTH_LONG)
                    .show()
            }
            ERROR_WRONG_PASSWORD -> {
                Toast.makeText(mainActivity, "${exc.message}", Toast.LENGTH_LONG)
                    .show()
            }
            else -> {
                Toast.makeText(mainActivity, "${exc.message}", Toast.LENGTH_LONG)
                    .show()
            }
        }
        when ((exc as FirebaseAuthWeakPasswordException).errorCode) {
            ERROR_ERROR_WEAK_PASSWORD -> {
                Toast.makeText(mainActivity, "${exc.message}", Toast.LENGTH_LONG)
                    .show()
            }
            else -> {
                Toast.makeText(mainActivity, "${exc.message}", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun signUpWithEmailSuccessful(user: FirebaseUser) {
        sendEmailVerification(user)
        mainActivity.uiUpdate(user)
    }


    private fun linkEmailToG(email: String, password: String) {
        val credential = EmailAuthProvider.getCredential(email, password)

        if (mainActivity.mAuth.currentUser != null) {
            mainActivity.mAuth.currentUser!!.linkWithCredential(credential)
                .addOnCompleteListener { task ->
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
            Toast.makeText(
                mainActivity,
                mainActivity.getString(R.string.enter_to_g),
                Toast.LENGTH_LONG
            )
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

    fun signOutGoogleAccount() {
        getSignInClient().signOut()
    }

    fun signInFirebaseWithGoogle(token: String) {
        val credential = GoogleAuthProvider.getCredential(token, null)
        mainActivity.mAuth.currentUser?.delete()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                mainActivity.mAuth.signInWithCredential(credential).addOnCompleteListener { task1 ->
                    if (task1.isSuccessful) {
                        Toast.makeText(mainActivity, "Sign in done", Toast.LENGTH_SHORT).show()
                        mainActivity.uiUpdate(task1.result.user)
                    }
                }
            }
        }
    }

    fun signInWithEmail(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            mainActivity.mAuth.currentUser?.delete()?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    mainActivity.mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task1 ->
                            if (task1.isSuccessful) {
                                mainActivity.uiUpdate(task1.result?.user)
                            } else {
                                signInEmailException(task1.exception!!, email, password)
                            }
                        }
                }
            }
        }
    }

    private fun signInEmailException(exc: Exception, email: String, password: String) {
        when ((exc as FirebaseAuthInvalidCredentialsException).errorCode) {
            ERROR_INVALID_EMAIL -> {
                Toast.makeText(mainActivity, "${exc.message}", Toast.LENGTH_LONG)
                    .show()
            }
            ERROR_WRONG_PASSWORD -> {
                Toast.makeText(mainActivity, "${exc.message}", Toast.LENGTH_LONG)
                    .show()
            }
            else -> {
                Toast.makeText(mainActivity, "${exc.message}", Toast.LENGTH_LONG)
                    .show()
            }
        }
        when ((exc as FirebaseAuthInvalidUserException).errorCode) {
            ERROR_USER_NOT_FOUND -> {}
            else -> {
                Toast.makeText(mainActivity, "${exc.message}", Toast.LENGTH_LONG)
                    .show()
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

    fun signInAnonymously(completeListener: CompleteListener) {
        mainActivity.mAuth.signInAnonymously().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                completeListener.onComplete()
                Toast.makeText(mainActivity, "Вы вошли как Гость", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(mainActivity, "Не удалось войти как Гость", Toast.LENGTH_LONG).show()
            }
        }
    }

    interface CompleteListener {
        fun onComplete()
    }
}