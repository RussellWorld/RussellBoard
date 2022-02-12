package com.russellworld.russellboard.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.russellworld.russellboard.R
import com.russellworld.russellboard.activity.EditAddActivity
import com.russellworld.russellboard.databinding.FragmentSelectImageItemBinding
import com.russellworld.russellboard.utilits.AdapterCallback
import com.russellworld.russellboard.utilits.ImageManager
import com.russellworld.russellboard.utilits.ImagePicker
import com.russellworld.russellboard.utilits.ItemTouchMoveCallback

class SelectImageRvAdapter(val adapterCallback: AdapterCallback) :
    RecyclerView.Adapter<SelectImageRvAdapter.ImageHolder>(),
    ItemTouchMoveCallback.ItemTouchAdapter {
    val mainArray = ArrayList<Bitmap>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val rootElement =
            FragmentSelectImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageHolder(rootElement, parent.context, this)
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        holder.setData(mainArray[position])
    }

    override fun getItemCount(): Int = mainArray.size

    override fun onMove(startPos: Int, targetPas: Int) {

        val targetItem = mainArray[targetPas]
        mainArray[targetPas] = mainArray[startPos]
        mainArray[startPos] = targetItem
        notifyItemMoved(startPos, targetPas)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onClear() {
        notifyDataSetChanged()
    }

    class ImageHolder(
        private val rootElement: FragmentSelectImageItemBinding,
        val context: Context,
        val adapter: SelectImageRvAdapter
    ) :
        RecyclerView.ViewHolder(rootElement.root) {

        fun setData(bitMap: Bitmap) {

            rootElement.btmEditImage.setOnClickListener {
                ImagePicker.getImages(
                    context as EditAddActivity,
                    1,
                    ImagePicker.REQUEST_CODE_GET_SINGLE_IMAGE
                )
                context.editImagePos = adapterPosition
            }

            rootElement.btnDeleteSelect.setOnClickListener {
                adapter.mainArray.removeAt(adapterPosition)
                adapter.notifyItemRemoved(adapterPosition)
                for (n in 0 until adapter.mainArray.size) adapter.notifyItemChanged(n)
                adapter.adapterCallback.onItemDelete()
            }

            rootElement.tvTitle.text = context.resources.getStringArray(R.array.title_array)[adapterPosition]
            ImageManager.chooseScaleType(rootElement.btmEditImage, bitMap)
            rootElement.imageViewDrug.setImageBitmap(bitMap)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateAdapter(newList: ArrayList<Bitmap>, needClear: Boolean) {
        if (needClear) mainArray.clear()
        mainArray.addAll(newList)
        notifyDataSetChanged()
    }
}