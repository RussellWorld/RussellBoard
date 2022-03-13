package com.russellworld.russellboard.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.russellworld.russellboard.databinding.AdListItemBinding
import com.russellworld.russellboard.model.Ad

class AdsRcAdapter(val auth: FirebaseAuth) : RecyclerView.Adapter<AdsRcAdapter.AdHolder>() {
    val adArray = ArrayList<Ad>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdHolder {
        val rootElement = AdListItemBinding.inflate(LayoutInflater.from(parent.context))
        return AdHolder(rootElement, auth)
    }

    override fun onBindViewHolder(holder: AdHolder, position: Int) {
        holder.setData(adArray[position])
    }

    override fun getItemCount(): Int = adArray.size

    fun updateAdapter(newList: ArrayList<Ad>) {
        adArray.clear()
        adArray.addAll(newList)
        notifyDataSetChanged()
    }

    class AdHolder(val rootElement: AdListItemBinding, val auth: FirebaseAuth) :
        RecyclerView.ViewHolder(rootElement.root) {

        fun setData(ad: Ad) {
            rootElement.apply {
                textViewTitle.text = ad.title
                tvDescription.text = ad.description
                tvPrice.text = ad.price
            }
            showEditPanel(isOwner(ad))
        }

        private fun isOwner(ad: Ad): Boolean {
            return ad.uid == auth.uid
        }

        private fun showEditPanel(isOwner: Boolean) {
            if (isOwner) {
                rootElement.editPanel.visibility = View.VISIBLE
            } else {
                rootElement.editPanel.visibility = View.GONE
            }
        }
    }
}