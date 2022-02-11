package com.russellworld.russellboard.utilits

import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.fxn.pix.Options
import com.fxn.pix.Pix
import com.russellworld.russellboard.activity.EditAddActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


object ImagePicker {

    const val MAX_IMAGE_COUNT = 3
    const val REQUEST_CODE_GET_IMAGES = 999
    const val REQUEST_CODE_GET_SINGLE_IMAGE = 998
    fun getImages(context: AppCompatActivity, imageCount: Int, rCode: Int) {
        val options = Options.init()
            .setRequestCode(rCode)
            .setCount(imageCount)
            .setFrontfacing(false)
            .setMode(Options.Mode.Picture)
            .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)
            .setPath("/pix/images")

        Pix.start(context, options)
    }

    fun showSelectedImages(resultCode: Int, requestCode: Int, data: Intent?, edActivity: EditAddActivity) {

        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == REQUEST_CODE_GET_IMAGES) {

            if (data != null) {

                val returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS)

                if (returnValue?.size!! > 1 && edActivity.chooseImageFragment == null) {

                    edActivity.openChooseImageFragment(returnValue)

                } else if (edActivity.chooseImageFragment != null) {

                    edActivity.chooseImageFragment?.updateAdapter(returnValue)

                } else if (returnValue.size == 1 && edActivity.chooseImageFragment == null) {

                    CoroutineScope(Dispatchers.Main).launch {
                        edActivity.rootElement.pBarEditAct.visibility = View.VISIBLE
                        val bitMapArray = ImageManager.imageResize(returnValue)
                        edActivity.rootElement.pBarEditAct.visibility = View.GONE
                        edActivity.imageAdapter.update(bitMapArray)
                    }
                }
            }

        } else if (resultCode == AppCompatActivity.RESULT_OK && requestCode == REQUEST_CODE_GET_SINGLE_IMAGE) {
            if (data != null) {

                val uri = data.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                edActivity.chooseImageFragment?.setSingleImage(uri?.get(0)!!, edActivity.editImagePos)
            }
        }
    }
}