package com.russellworld.russellboard

import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.russellworld.russellboard.databinding.ActivityMainBinding
import com.russellworld.russellboard.dialoghelper.DialogHelper
import com.russellworld.russellboard.utilits.SIGN_IN_STATE
import com.russellworld.russellboard.utilits.SIGN_UP_STATE

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var rootMainElement: ActivityMainBinding
    private val dialogHelper = DialogHelper(this)
    val mAuth = FirebaseAuth.getInstance()
    private lateinit var tvAccount: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootMainElement = ActivityMainBinding.inflate(layoutInflater)
        setContentView(rootMainElement.root)
        init()
    }

    override fun onStart() {
        super.onStart()
        uiUpdate(mAuth.currentUser)
    }

    private fun init() {
        val toggle =
            ActionBarDrawerToggle(
                this, rootMainElement.drawerLayout, rootMainElement.mainContent.mainToolbar, R.string.open, R.string.close
            )
        rootMainElement.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        rootMainElement.drawerNavView.setNavigationItemSelectedListener(this)
        tvAccount = rootMainElement.drawerNavView.getHeaderView(0).findViewById(R.id.drawer_header_textv)
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
                dialogHelper.createSignDialog(SIGN_UP_STATE)
            }
            R.id.acc_sign_in -> {
                dialogHelper.createSignDialog(SIGN_IN_STATE)
            }
            R.id.acc_sign_out -> {
                uiUpdate(null)
                mAuth.signOut()
            }
        }
        rootMainElement.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    fun uiUpdate(user: FirebaseUser?) {
        tvAccount.text = if (user == null) {
            resources.getString(R.string.not_reg)
        } else {
            user.email
        }
    }
}