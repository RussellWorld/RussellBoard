package com.russellworld.russellboard.model

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.russellworld.russellboard.utilits.AD_NOTE
import com.russellworld.russellboard.utilits.FAVS_NODE
import com.russellworld.russellboard.utilits.INFO_NODE
import com.russellworld.russellboard.utilits.MAIN_NODE

class DbManager {
    val database = Firebase.database.getReference(MAIN_NODE)
    val auth = Firebase.auth

    fun publishAdd(ad: Ad, finishWorkListener: FinishWorkListener) {
        if (auth.uid != null) {
            database.child(ad.key ?: "empty")
                .child(auth.uid!!).child(AD_NOTE).setValue(ad)
                .addOnCompleteListener {
                    finishWorkListener.onFinish()
                }
        }
    }

    fun adViewed(ad: Ad) {
        var counter = ad.viewsCounter.toInt()
        counter++

        if (auth.uid != null) {
            database.child(ad.key ?: "empty")
                .child(INFO_NODE).setValue(InfoItem(counter.toString(), ad.emailCounter, ad.callsCounter))

        }
    }

    fun onFavClick(ad: Ad, finishWorkListener: FinishWorkListener) {
        if (ad.isFav) {
            removeFromFavs(ad, finishWorkListener)
        } else {
            addToFavs(ad, finishWorkListener)
        }
    }

    fun addToFavs(ad: Ad, finishWorkListener: FinishWorkListener) {
        ad.key?.let {
            auth.uid?.let { uid ->
                database.child(it).child(FAVS_NODE).child(uid)
                    .setValue(uid)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            finishWorkListener.onFinish()
                        }
                    }
            }
        }
    }

    private fun removeFromFavs(ad: Ad, finishWorkListener: FinishWorkListener) {
        ad.key?.let {
            auth.uid?.let { uid ->
                database.child(it).child(FAVS_NODE).child(uid)
                    .removeValue()
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            finishWorkListener.onFinish()
                        }
                    }
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
                    var ad: Ad? = null
                    item.children.forEach {
                        if (ad == null) ad = it.child(AD_NOTE).getValue(Ad::class.java)
                    }
                    val infoItem = item.child(INFO_NODE).getValue(InfoItem::class.java)

                    val favCounter = item.child(FAVS_NODE).childrenCount
                    val isFav = auth.uid?.let {
                        item.child(FAVS_NODE).child(it)
                            .getValue(String::class.java)
                    }
                    ad?.isFav = isFav != null
                    ad?.favCounter = favCounter.toString()

                    ad?.viewsCounter = infoItem?.viewsCounter ?: "0"
                    ad?.emailCounter = infoItem?.emailsCounter ?: "0"
                    ad?.callsCounter = infoItem?.callsCounter ?: "0"
                    if (ad != null) adArray.add(ad!!)
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