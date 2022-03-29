package com.russellworld.russellboard.utilits

import android.content.Intent
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.russellworld.russellboard.R
import com.russellworld.russellboard.activity.EditAddActivity
import io.ak1.pix.helpers.PixEventCallback
import io.ak1.pix.helpers.addPixToActivity
import io.ak1.pix.models.Mode
import io.ak1.pix.models.Options
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


object ImagePicker {

    const val MAX_IMAGE_COUNT = 3
    const val REQUEST_CODE_GET_IMAGES = 999
    const val REQUEST_CODE_GET_SINGLE_IMAGE = 998
    fun getOptions(imageCount: Int): Options {

        return Options().apply {
            count = imageCount
            isFrontFacing = false
            mode = Mode.Picture
            path = "/pix/images"
        }
    }

    fun launcherImages(
        edActivity: EditAddActivity, launcher: ActivityResultLauncher<Intent>?,
        imageCount: Int
    ) {
        edActivity.addPixToActivity(R.id.placeHolder, getOptions(imageCount)) { result ->
            when (result.status) {
                PixEventCallback.Status.SUCCESS -> {
                    val fragmentList = edActivity.supportFragmentManager.fragments
                    fragmentList.forEach {
                        if (it.isVisible) {
                            edActivity.supportFragmentManager
                                .beginTransaction()
                                .remove(it)
                                .commit()
                        }
                    }
                }
                PixEventCallback.Status.BACK_PRESSED -> {}
            }
        }
    }

    fun getLauncherForMultiSelectImages(edActivity: EditAddActivity): ActivityResultLauncher<Intent> {
        return edActivity.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result: ActivityResult ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                if (result.data != null) {

                    val returnValue = result.data?.getStringArrayListExtra(Pix.IMAGE_RESULTS)

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
            }
        }
    }

    fun getLauncherForSingleImages(edActivity: EditAddActivity): ActivityResultLauncher<Intent> {
        return edActivity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                if (result.data != null) {

                    val uri = result.data?.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                    edActivity.chooseImageFragment?.setSingleImage(uri?.get(0)!!, edActivity.editImagePos)
                }
            }
        }
    }
}