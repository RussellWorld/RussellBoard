package com.russellworld.russellboard.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.russellworld.russellboard.model.Ad
import com.russellworld.russellboard.model.DbManager

class FirebaseViewModel : ViewModel(){
    private val dbManager = DbManager()
    val liveAdsData = MutableLiveData<ArrayList<Ad>>()
    fun loadAllAds(){
        dbManager.readDataFromDb(object: DbManager.ReadDataCallBack{
            override fun readData(arrayList: ArrayList<Ad>) {
             liveAdsData.value = arrayList
            }
        })
    }
}