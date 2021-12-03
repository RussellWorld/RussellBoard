package com.russellworld.russellboard

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.russellworld.russellboard.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var _binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        init()
    }

    private fun init() {
        val toggle =
            ActionBarDrawerToggle(
                this, _binding.drawerLayout, _binding.mainContent.mainToolbar, R.string.open, R.string.close
            )
        _binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        _binding.drawerNavView.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_my_adds -> {
                Toast.makeText(this, "${item.title}", Toast.LENGTH_SHORT).show()
            }
            R.id.add_сar -> {
                Toast.makeText(this, "${item.title}", Toast.LENGTH_SHORT).show()
            }
            R.id.add_PC -> {
                Toast.makeText(this, "${item.title}", Toast.LENGTH_SHORT).show()
            }
            R.id.add_smartphone -> {
                Toast.makeText(this, "${item.title}", Toast.LENGTH_SHORT).show()
            }
            R.id.add_appliances -> {
                Toast.makeText(this, "${item.title}", Toast.LENGTH_SHORT).show()
            }
            R.id.acc_registration -> {
                Toast.makeText(this, "${item.title}", Toast.LENGTH_SHORT).show()
            }
            R.id.acc_sign_in -> {
                Toast.makeText(this, "${item.title}", Toast.LENGTH_SHORT).show()
            }
            R.id.acc_sign_out -> {
                Toast.makeText(this, "${item.title}", Toast.LENGTH_SHORT).show()
            }
        }
        _binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}