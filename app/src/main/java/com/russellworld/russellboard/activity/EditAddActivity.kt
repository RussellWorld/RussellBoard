package com.russellworld.russellboard.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
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
    fun onClickSelectCountry(view: View){
        val listCountry = CityHelper.getAllCountries(this)
        dialog.showSpinnerDialog(this, listCountry)

    }
}