package com.russellworld.russellboard.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.russellworld.russellboard.R
import com.russellworld.russellboard.databinding.ActivityEditAddBinding
import com.russellworld.russellboard.utilits.CityHelper

class EditAddActivity : AppCompatActivity() {
    private lateinit var rootElement: ActivityEditAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootElement = ActivityEditAddBinding.inflate(layoutInflater)
        setContentView(rootElement.root)

        val adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, CityHelper.getAllCountries(this))
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        rootElement.spCountry.adapter = adapter
    }
}