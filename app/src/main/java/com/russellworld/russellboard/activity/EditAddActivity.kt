package com.russellworld.russellboard.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.russellworld.russellboard.R
import com.russellworld.russellboard.databinding.ActivityEditAddBinding
import com.russellworld.russellboard.dialogs.DialogSpinnerHelper
import com.russellworld.russellboard.utilits.CityHelper

class EditAddActivity : AppCompatActivity() {
    lateinit var rootElement: ActivityEditAddBinding
    private val dialog = DialogSpinnerHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootElement = ActivityEditAddBinding.inflate(layoutInflater)
        setContentView(rootElement.root)

        init()
    }

    private fun init() {

    }

    //OnClocks
    fun onClickSelectCountry(view: View) {
        val listCountry = CityHelper.getAllCountries(this)
        dialog.showSpinnerDialog(this, listCountry, rootElement.tvCountry)
        if (rootElement.tvCity.text.toString() != getString(R.string.select_city)){
            rootElement.tvCity.text = getString(R.string.select_city)
        }
    }

    fun onClickSelectCity(view: View) {
        val selectedCountry = rootElement.tvCountry.text.toString()
        if (selectedCountry != getString(R.string.select_country)) {
            val listCity = CityHelper.getAllCities(selectedCountry, this)
            dialog.showSpinnerDialog(this, listCity, rootElement.tvCity)
        } else {
            Toast.makeText(this, "No country selected", Toast.LENGTH_LONG).show()
        }
    }
}