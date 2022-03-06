package com.russellworld.russellboard.database

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.russellworld.russellboard.model.Ad

class DbManager {
    val database = Firebase.database.getReference("Main")
    val auth = Firebase.auth

    fun publishAdd(ad: Ad) {
        if (auth.uid != null) {
            database.child(ad.key ?: "empty").child(auth.uid!!).child("ad").setValue(ad)
        }
    }

    fun readDataFromDb() {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (item in snapshot.children) {
                    val ad = item.children.iterator().next().child("ad").getValue(Ad::class.java)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}