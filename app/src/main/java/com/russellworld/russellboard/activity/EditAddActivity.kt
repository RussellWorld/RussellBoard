package com.russellworld.russellboard.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import com.fxn.utility.PermUtil
import com.russellworld.russellboard.R
import com.russellworld.russellboard.adapters.ImageAdapter
import com.russellworld.russellboard.database.DbManager
import com.russellworld.russellboard.databinding.ActivityEditAddBinding
import com.russellworld.russellboard.dialogs.DialogSpinnerHelper
import com.russellworld.russellboard.fragments.FragmentCloseInterface
import com.russellworld.russellboard.fragments.ImageListFragment
import com.russellworld.russellboard.model.Ad
import com.russellworld.russellboard.utilits.CityHelper
import com.russellworld.russellboard.utilits.ImagePicker

class EditAddActivity : AppCompatActivity(), FragmentCloseInterface {

    lateinit var rootElement: ActivityEditAddBinding
    var chooseImageFragment: ImageListFragment? = null
    val dialog = DialogSpinnerHelper()
    lateinit var imageAdapter: ImageAdapter
    var editImagePos = 0
    val dbManager = DbManager(null)
    var launcherMultiSelectImage: ActivityResultLauncher<Intent>? = null
    var launcherSinglSelectImage: ActivityResultLauncher<Intent>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootElement = ActivityEditAddBinding.inflate(layoutInflater)
        setContentView(rootElement.root)

        init()
    }


    fun openChooseImageFragment(newList: ArrayList<String>?) {
        chooseImageFragment = ImageListFragment(this, newList)
        rootElement.scrollViewMain.visibility = View.GONE
        val fm = supportFragmentManager.beginTransaction()
        fm.replace(R.id.placeHolder, chooseImageFragment!!)
        fm.commit()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ImagePicker.getOptions(3)
                } else {
                    Toast.makeText(
                        this,
                        "Approve permissions to open Pix ImagePicker", Toast.LENGTH_LONG
                    )
                        .show()
                }
                return
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun init() {
        imageAdapter = ImageAdapter()
        rootElement.vpImages.adapter = imageAdapter
        launcherMultiSelectImage = ImagePicker.getLauncherForMultiSelectImages(this)
        launcherSinglSelectImage = ImagePicker.getLauncherForSingleImages(this)
    }


    fun onClickSelectCountry(view: View) {
        val listCountry = CityHelper.getAllCountries(this)
        dialog.showSpinnerDialog(this, listCountry, rootElement.tvCountry)
        if (rootElement.tvCity.text.toString() != getString(R.string.select_city)) {
            rootElement.tvCity.text = getString(R.string.select_city)
        }
    }


    fun onClickSelectCategory(view: View) {

        val listCategory = resources.getStringArray(R.array.category).toMutableList() as ArrayList
        dialog.showSpinnerDialog(this, listCategory, rootElement.tvCategory)

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

    fun onClickGetImages(view: View) {
        if (imageAdapter.mainArray.size == 0) {
            ImagePicker.launcherImages(this, launcherMultiSelectImage, 3)
        } else {
            openChooseImageFragment(null)
            chooseImageFragment?.updateAdapterFromEdit(imageAdapter.mainArray)
        }
    }

    fun onClickPublish(view: View) {
        dbManager.publishAdd(fillAd())
    }

    private fun fillAd(): Ad {
        val ad: Ad
        rootElement.apply {
            ad = Ad(
                tvCountry.text.toString(),
                tvCity.text.toString(),
                edTel.text.toString(),
                edIndex.text.toString(),
                checkBoxWithSend.isChecked.toString(),
                tvCategory.text.toString(),
                tvTitleTitle.text.toString(),
                edPrice.text.toString(),
                edDescription.text.toString(),
                dbManager.database.push().key,
                dbManager.auth.uid
            )
        }
        return ad
    }

    override fun onFragClose(list: ArrayList<Bitmap>) {
        rootElement.scrollViewMain.visibility = View.VISIBLE
        imageAdapter.update(list)
        chooseImageFragment = null
    }
}