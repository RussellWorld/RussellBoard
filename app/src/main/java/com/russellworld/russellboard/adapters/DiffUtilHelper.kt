package com.russellworld.russellboard.adapters

import androidx.recyclerview.widget.DiffUtil
import com.russellworld.russellboard.model.Ad

class DiffUtilHelper(val oldList: ArrayList<Ad>, val newList: ArrayList<Ad>) : DiffUtil.Callback() {


    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].key == newList[newItemPosition].key
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}