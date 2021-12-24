package com.russellworld.russellboard.utilits

import android.content.Context
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.util.*

object CityHelper {
    fun getAllCountries(context: Context): ArrayList<String> {
        val tempArray = ArrayList<String>()
        try {
            val inputStream: InputStream = context.assets.open("countriesToCities.json")
            val size: Int = inputStream.available()
            val byteArray = ByteArray(size)
            inputStream.read(byteArray)
            val jsonFile = String(byteArray)
            val jsonObject = JSONObject(jsonFile)
            val countriesNames = jsonObject.names()
            if (countriesNames != null) {
                for (n in 0 until countriesNames.length()) {
                    tempArray.add(countriesNames.getString(n))
                }
            }

        } catch (e: IOException) {

        }
        return tempArray
    }

    fun getAllCities(country: String, context: Context): ArrayList<String> {
        val tempArray = ArrayList<String>()

        try {
            val inputStream: InputStream = context.assets.open("countriesToCities.json")
            val size: Int = inputStream.available()
            val byteArray = ByteArray(size)
            inputStream.read(byteArray)
            val jsonFile = String(byteArray)
            val jsonObject = JSONObject(jsonFile)
            val cityNames = jsonObject.getJSONArray(country)
            for (n in 0 until cityNames.length()) {
                tempArray.add(cityNames.getString(n))
            }

        } catch (e: IOException) {

        }
        return tempArray
    }

    fun filterListData(list: ArrayList<String>, searchText: String?): ArrayList<String> {
        val tempList = ArrayList<String>()
        tempList.clear()
        if (searchText == null) {
            tempList.add("No result")
            return tempList
        }
        for (selection: String in list) {
            if (selection.toLowerCase(Locale.ROOT).startsWith(searchText.toLowerCase(Locale.ROOT))) {
                tempList.add(selection)
            }

        }
        if (tempList.size == 0) tempList.add("No result")
        return tempList
    }
}