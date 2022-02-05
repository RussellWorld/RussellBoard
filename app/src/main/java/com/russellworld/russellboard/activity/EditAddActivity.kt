package com.russellworld.russellboard.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fxn.pix.Pix
import com.fxn.utility.PermUtil
import com.russellworld.russellboard.R
import com.russellworld.russellboard.adapters.ImageAdapter
import com.russellworld.russellboard.databinding.ActivityEditAddBinding
import com.russellworld.russellboard.dialogs.DialogSpinnerHelper
import com.russellworld.russellboard.fragments.FragmentCloseInterface
import com.russellworld.russellboard.fragments.ImageListFragment
import com.russellworld.russellboard.utilits.CityHelper
import com.russellworld.russellboard.utilits.ImagePicker

class EditAddActivity : AppCompatActivity(), FragmentCloseInterface {

    lateinit var rootElement: ActivityEditAddBinding
    private var chooseImageFragment: ImageListFragment? = null
    private val dialog = DialogSpinnerHelper()
    private lateinit var imageAdapter: ImageAdapter
    var editImagePos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootElement = ActivityEditAddBinding.inflate(layoutInflater)
        setContentView(rootElement.root)

        init()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == ImagePicker.REQUEST_CODE_GET_IMAGES) {

            if (data != null) {

                val returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                if (returnValue?.size!! > 1 && chooseImageFragment == null) {

                    openChooseImageFragment(returnValue)

                } else if (chooseImageFragment != null) {

                    chooseImageFragment?.updateAdapter(returnValue)
                }
            }

        } else if (resultCode == RESULT_OK && requestCode == ImagePicker.REQUEST_CODE_GET_SINGLE_IMAGE) {
            if (data != null) {

                val uri = data.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                chooseImageFragment?.setSingleImage(uri?.get(0)!!, editImagePos)
            }
        }
    }

    private fun openChooseImageFragment(newList: ArrayList<String>?) {
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
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ImagePicker.getImages(this, 3, ImagePicker.REQUEST_CODE_GET_IMAGES)
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
    }

    private fun init() {
        imageAdapter = ImageAdapter()
        rootElement.vpImages.adapter = imageAdapter
    }

    //OnClocks
    fun onClickSelectCountry(view: View) {
        val listCountry = CityHelper.getAllCountries(this)
        dialog.showSpinnerDialog(this, listCountry, rootElement.tvCountry)
        if (rootElement.tvCity.text.toString() != getString(R.string.select_city)) {
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

    fun onClickGetImages(view: View) {
        if (imageAdapter.mainArray.size == 0) {
            ImagePicker.getImages(this, 3, ImagePicker.REQUEST_CODE_GET_IMAGES)
        } else {
            openChooseImageFragment(null)
            chooseImageFragment?.updateAdapterFromEdit(imageAdapter.mainArray)
        }
    }

    override fun onFragClose(list: ArrayList<Bitmap>) {
        rootElement.scrollViewMain.visibility = View.VISIBLE
        imageAdapter.update(list)
        chooseImageFragment = null
    }
}