package com.russellworld.russellboard.model

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DbManager {
    val database = Firebase.database.getReference("Main")
    val auth = Firebase.auth

    fun publishAdd(ad: Ad, finishWorkListener: FinishWorkListener) {
        if (auth.uid != null) {
            database.child(ad.key ?: "empty")
                .child(auth.uid!!).child("ad").setValue(ad)
                .addOnCompleteListener {
                    finishWorkListener.onFinish()
                }
        }
    }

    fun getMyAds(readDataCallBack: ReadDataCallBack?) {
        val query = database.orderByChild(auth.uid + "/ad/uid").equalTo(auth.uid)
        readDataFromDb(query, readDataCallBack)
    }

    fun getAllAds(readDataCallBack: ReadDataCallBack?) {
        val query = database.orderByChild(auth.uid + "/ad/price")
        readDataFromDb(query, readDataCallBack)
    }

    fun deleteAd(ad: Ad, listener: FinishWorkListener) {
        if (ad.key == null || ad.uid == null) return
        database.child(ad.key).child(ad.uid).removeValue().addOnCompleteListener {
            if (it.isSuccessful) listener.onFinish()
        }
    }

    private fun readDataFromDb(query: Query, readDataCallBack: ReadDataCallBack?) {
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val adArray = ArrayList<Ad>()
                for (item in snapshot.children) {
                    val ad = item.children.iterator().next().child("ad").getValue(Ad::class.java)
                    if (ad != null) adArray.add(ad)
                }
                readDataCallBack?.readData(adArray)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    interface ReadDataCallBack {
        fun readData(arrayList: ArrayList<Ad>)
    }

    interface FinishWorkListener {
        fun onFinish()
    }
}