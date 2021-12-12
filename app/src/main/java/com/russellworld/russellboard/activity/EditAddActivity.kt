package com.russellworld.russellboard.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.russellworld.russellboard.R
import com.russellworld.russellboard.databinding.ActivityEditAddBinding

class EditAddActivity : AppCompatActivity() {
    private lateinit var rootElement: ActivityEditAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootElement = ActivityEditAddBinding.inflate(layoutInflater)
        setContentView(rootElement.root)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.id_new_adds) {
            val i = Intent(this, EditAddActivity::class.java)
            startActivity(i)
        }
        return super.onOptionsItemSelected(item)
    }
}