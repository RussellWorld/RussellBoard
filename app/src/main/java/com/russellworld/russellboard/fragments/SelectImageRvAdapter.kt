package com.russellworld.russellboard.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.russellworld.russellboard.R
import com.russellworld.russellboard.activity.EditAddActivity
import com.russellworld.russellboard.utilits.ImagePicker
import com.russellworld.russellboard.utilits.ItemTouchMoveCallback

class SelectImageRvAdapter : RecyclerView.Adapter<SelectImageRvAdapter.ImageHolder>(),
    ItemTouchMoveCallback.ItemTouchAdapter {
    val mainArray = ArrayList<Bitmap>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.fragment_select_image_item, parent, false)
        return ImageHolder(view, parent.context, this)
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

    class ImageHolder(itemView: View, val context: Context, val adapter: SelectImageRvAdapter) :
        RecyclerView.ViewHolder(itemView) {
        private lateinit var tvTitle: TextView
        private lateinit var image: ImageView
        private lateinit var imEditImage: ImageButton
        private lateinit var imDeleteImage: ImageButton

        fun setData(bitMap: Bitmap) {
            tvTitle = itemView.findViewById(R.id.tvTitile)
            image = itemView.findViewById(R.id.inDrug)
            imDeleteImage = itemView.findViewById(R.id.btnDeleteSelect)
            imEditImage = itemView.findViewById(R.id.btmEditImage)

            imEditImage.setOnClickListener {
                ImagePicker.getImages(
                    context as EditAddActivity,
                    1,
                    ImagePicker.REQUEST_CODE_GET_SINGLE_IMAGE
                )
                context.editImagePos = adapterPosition

            }

            imDeleteImage.setOnClickListener {

                adapter.mainArray.removeAt(adapterPosition)
                adapter.notifyItemRemoved(adapterPosition)
                for (n in 0 until adapter.mainArray.size) adapter.notifyItemChanged(n)
            }

            tvTitle.text = context.resources.getStringArray(R.array.title_array)[adapterPosition]
            image.setImageBitmap(bitMap)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateAdapter(newList: ArrayList<Bitmap>, needClear: Boolean) {
        if (needClear) mainArray.clear()
        mainArray.addAll(newList)
        notifyDataSetChanged()
    }
}