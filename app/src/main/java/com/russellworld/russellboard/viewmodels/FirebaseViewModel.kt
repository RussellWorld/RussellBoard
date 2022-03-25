package com.russellworld.russellboard.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.russellworld.russellboard.model.Ad
import com.russellworld.russellboard.model.DbManager

class FirebaseViewModel : ViewModel() {
    private val dbManager = DbManager()
    val liveAdsData = MutableLiveData<ArrayList<Ad>?>()
    fun loadAllAds() {
        dbManager.getAllAds(object : DbManager.ReadDataCallBack {
            override fun readData(arrayList: ArrayList<Ad>) {
                liveAdsData.value = arrayList
            }
        })
    }

    fun onFavClick(ad: Ad) {
        dbManager.onFavClick(ad, object : DbManager.FinishWorkListener {
            override fun onFinish() {
                val updateList = liveAdsData.value
                val position = updateList?.indexOf(ad)
                if (position != -1) {
                    position?.let {
                        val favCounter =
                            if (ad.isFav) ad.favCounter.toInt() - 1 else ad.favCounter.toInt() + 1
                        updateList[position] =
                            updateList[position].copy(isFav = !ad.isFav, favCounter = favCounter.toString())
                    }
                }
                liveAdsData.postValue(updateList)
            }
        })
    }

    fun adViewed(ad: Ad) {
        dbManager.adViewed(ad)
    }

    fun loadMyAds() {
        dbManager.getMyAds(object : DbManager.ReadDataCallBack {
            override fun readData(arrayList: ArrayList<Ad>) {
                liveAdsData.value = arrayList
            }
        })
    }

    fun loadMyFavs() {
        dbManager.getMyFavs(object : DbManager.ReadDataCallBack {
            override fun readData(arrayList: ArrayList<Ad>) {
                liveAdsData.value = arrayList
            }
        })
    }

    fun deleteItem(ad: Ad) {
        dbManager.deleteAd(ad, object : DbManager.FinishWorkListener {
            override fun onFinish() {
                val updatedList = liveAdsData.value
                updatedList?.remove(ad)
                liveAdsData.postValue(updatedList)
            }
        })
    }
}