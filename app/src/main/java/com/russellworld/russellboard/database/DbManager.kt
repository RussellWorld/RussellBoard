package com.russellworld.russellboard.database

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.russellworld.russellboard.model.Ad

class DbManager {
    val datebase = Firebase.database.getReference("Main")
    val auth = Firebase.auth

    fun publishAdd(ad: Ad) {
        if (auth.uid != null) {
            datebase.child(ad.key ?: "empty").child(auth.uid!!).child("ad").setValue(ad)
        }
    }
}