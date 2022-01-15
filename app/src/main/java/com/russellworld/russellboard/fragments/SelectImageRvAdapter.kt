package com.russellworld.russellboard.fragments

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.russellworld.russellboard.R
import com.russellworld.russellboard.utilits.ItemTouchMoveCallback

class SelectImageRvAdapter : RecyclerView.Adapter<SelectImageRvAdapter.ImageHolder>(),
    ItemTouchMoveCallback.ItemTouchAdapter {
    private val mainArray = ArrayList<SelectImageItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.fragment_select_image_item, parent, false)
        return ImageHolder(view)
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

    class ImageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var tvTitle: TextView
        lateinit var image: ImageView
        fun setData(item: SelectImageItem) {
            tvTitle = itemView.findViewById(R.id.tvTitile)
            image = itemView.findViewById(R.id.inDrug)
            tvTitle.text = item.title
            image.setImageURI(Uri.parse(item.imageUri))
        }
    }

    fun updateAdapter(newList: ArrayList<SelectImageItem>) {
        mainArray.clear()
        mainArray.addAll(newList)
        notifyDataSetChanged()
    }


}