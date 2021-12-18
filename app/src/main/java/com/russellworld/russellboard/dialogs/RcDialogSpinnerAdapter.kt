package com.russellworld.russellboard.dialogs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.russellworld.russellboard.R

class RcDialogSpinnerAdapter : RecyclerView.Adapter<RcDialogSpinnerAdapter.SpViewHolder>() {
    val mainList = ArrayList<String>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sp_list_item, parent, false)
        return SpViewHolder(view)
    }

    override fun onBindViewHolder(holder: SpViewHolder, position: Int) {
        holder.setData(mainList[position])
    }

    override fun getItemCount(): Int = mainList.size

    class SpViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvSpItem = itemView.findViewById<TextView>(R.id.tvSpItem)
        fun setData(text: String) {
            tvSpItem.text = text
        }
    }

    fun updateAdapter(list: ArrayList<String>) {
        mainList.clear()
        mainList.addAll(list)
        notifyDataSetChanged()
    }

}
