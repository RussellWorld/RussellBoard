package com.russellworld.russellboard.fragments

import android.app.Activity
import android.graphics.Bitmap
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.russellworld.russellboard.R
import com.russellworld.russellboard.dialoghelper.ProgressDialog
import com.russellworld.russellboard.utilits.AdapterCallback
import com.russellworld.russellboard.utilits.ImageManager
import com.russellworld.russellboard.utilits.ImagePicker
import com.russellworld.russellboard.utilits.ItemTouchMoveCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ImageListFragment(
    private val fragCloseInterface: FragmentCloseInterface,
    private val newList: ArrayList<String>?
) :
    BaseSelectImageFragment(),
    AdapterCallback {

    val adapter = SelectImageRvAdapter(this)
    val dragCallback = ItemTouchMoveCallback(adapter)
    val touchHelper = ItemTouchHelper(dragCallback)
    private var job: Job? = null
    private var addImageItem: MenuItem? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar()
        rootElement.apply {
            touchHelper.attachToRecyclerView(rcViewSelectImage)
            rcViewSelectImage.layoutManager = LinearLayoutManager(activity)
            rcViewSelectImage.adapter = adapter

            if (newList != null) {
                resizeSelectedImage(newList, true)
            }
        }

    }

    override fun onItemDelete() {
        addImageItem?.isVisible = true
    }

    fun updateAdapterFromEdit(bitmapList: ArrayList<Bitmap>) {
        adapter.updateAdapter(bitmapList, true)
    }

    private fun setUpToolbar() {
        rootElement.apply {

            tbChooseMenu.inflateMenu(R.menu.choose_image_menu)
            addImageItem = tbChooseMenu.menu.findItem(R.id.menu_choose_add_image)
            val deleteItem = tbChooseMenu.menu.findItem(R.id.menu_choose_delete_image)

            tbChooseMenu.setNavigationOnClickListener {
                activity?.supportFragmentManager
                    ?.beginTransaction()?.remove(this@ImageListFragment)?.commit()
            }
            deleteItem.setOnMenuItemClickListener {
                adapter.updateAdapter(ArrayList(), true)
                addImageItem?.isVisible = true
                true
            }

            addImageItem?.setOnMenuItemClickListener {
                val imageCount = ImagePicker.MAX_IMAGE_COUNT - adapter.mainArray.size
                ImagePicker.getImages(
                    activity as AppCompatActivity,
                    imageCount,
                    ImagePicker.REQUEST_CODE_GET_IMAGES
                )
                true
            }
        }
    }

    fun updateAdapter(newList: ArrayList<String>) {
        resizeSelectedImage(newList, false)
    }

    fun setSingleImage(uri: String, pos: Int) {
        val pBar = rootElement.rcViewSelectImage[pos].findViewById<ProgressBar>(R.id.pBarSelectItem)
        job = CoroutineScope(Dispatchers.Main).launch {
            pBar.visibility = View.VISIBLE
            val bitmapList = ImageManager.imageResize(arrayListOf(uri))
            pBar.visibility = View.GONE
            adapter.mainArray[pos] = bitmapList[0]
            adapter.notifyItemChanged(pos)
        }
    }

    private fun resizeSelectedImage(newList: ArrayList<String>, needClear: Boolean) {

        job = CoroutineScope(Dispatchers.Main).launch {
            val dialog = ProgressDialog.createProgressDialog(activity as Activity)
            val bitmapList = ImageManager.imageResize(newList)
            dialog.dismiss()
            adapter.updateAdapter(bitmapList, needClear)
            if (adapter.mainArray.size > 2) addImageItem?.isVisible = false
        }
    }

    override fun onDetach() {
        super.onDetach()
        fragCloseInterface.onFragClose(adapter.mainArray)
        job?.cancel()
    }


}