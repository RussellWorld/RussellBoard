package com.russellworld.russellboard.database

import com.russellworld.russellboard.model.Ad

interface ReadDataCallBack {
    fun readData(arrayList: ArrayList<Ad>)
}