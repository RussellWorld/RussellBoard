package com.russellworld.russellboard

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.russellworld.russellboard.activity.EditAddActivity
import com.russellworld.russellboard.adapters.AdsRcAdapter
import com.russellworld.russellboard.databinding.ActivityMainBinding
import com.russellworld.russellboard.dialoghelper.DialogHelper
import com.russellworld.russellboard.model.Ad
import com.russellworld.russellboard.utilits.SIGN_IN_REQUEST_CODE
import com.russellworld.russellboard.utilits.SIGN_IN_STATE
import com.russellworld.russellboard.utilits.SIGN_UP_STATE
import com.russellworld.russellboard.viewmodels.FirebaseViewModel


class MainActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener, AdsRcAdapter.ItemsListener {
    private lateinit var rootMainElement: ActivityMainBinding
    private val dialogHelper = DialogHelper(this)
    val mAuth = Firebase.auth
    private lateinit var tvAccount: TextView
    val adapter = AdsRcAdapter(this)
    private val firebaseViewModel: FirebaseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootMainElement = ActivityMainBinding.inflate(layoutInflater)
        setContentView(rootMainElement.root)
        init()
        initRecyclerView()
        initViewModel()
        firebaseViewModel.loadAllAds()
        bottomMenuOnClick()
    }

    override fun onResume() {
        super.onResume()
        rootMainElement.mainContent.bNavView.selectedItemId = R.id.id_home
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == SIGN_IN_REQUEST_CODE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) dialogHelper.accountHelper.signInFirebaseWithGoogle(account.idToken!!)
            } catch (e: Exception) {
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onStart() {
        super.onStart()
        uiUpdate(mAuth.currentUser)
    }

    private fun initViewModel() {
        firebaseViewModel.liveAdsData.observe(this) {
            if (it != null) {
                adapter.updateAdapter(it)
            }
        }
    }

    private fun init() {
        setSupportActionBar(rootMainElement.mainContent.mainToolbar)
        val toggle =
            ActionBarDrawerToggle(
                this,
                rootMainElement.drawerLayout,
                rootMainElement.mainContent.mainToolbar,
                R.string.open,
                R.string.close
            )
        rootMainElement.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        rootMainElement.drawerNavView.setNavigationItemSelectedListener(this)
        tvAccount = rootMainElement.drawerNavView.getHeaderView(0).findViewById(R.id.drawer_header_textv)
    }

    private fun bottomMenuOnClick() {
        rootMainElement.mainContent.bNavView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.id_new_ad -> {
                    val i = Intent(this, EditAddActivity::class.java)
                    startActivity(i)
                }
                R.id.id_my_ads -> {
                    firebaseViewModel.loadMyAds()
                    rootMainElement.mainContent.mainToolbar.title = getString(R.string.add_my_adds)
                }
                R.id.id_favs -> {
                    Toast.makeText(this@MainActivity, "Favs", Toast.LENGTH_SHORT).show()
                }
                R.id.id_home -> {
                    firebaseViewModel.loadAllAds()
                    rootMainElement.mainContent.mainToolbar.title = getString(R.string.add_my_def)
                }
            }
            true
        }
    }

    private fun initRecyclerView() {
        rootMainElement.apply {
            mainContent.rcMainView.layoutManager = LinearLayoutManager(this@MainActivity)
            mainContent.rcMainView.adapter = adapter

        }
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
                dialogHelper.accountHelper.signOutGoogleAccount()
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

    override fun onDeleteItem(ad: Ad) {
        firebaseViewModel.deleteItem(ad)
    }

    override fun onAdViewed(ad: Ad) {
        firebaseViewModel.adViewed(ad)
    }

    override fun onFavClicked(ad: Ad) {
       firebaseViewModel.onFavClick(ad)
    }
}