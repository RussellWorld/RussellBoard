package com.russellworld.russellboard.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.russellworld.russellboard.MainActivity
import com.russellworld.russellboard.R
import com.russellworld.russellboard.activity.EditAddActivity
import com.russellworld.russellboard.databinding.AdListItemBinding
import com.russellworld.russellboard.model.Ad
import com.russellworld.russellboard.utilits.ADS_DATA
import com.russellworld.russellboard.utilits.EDIT_STATE

class AdsRcAdapter(val activity: MainActivity) : RecyclerView.Adapter<AdsRcAdapter.AdHolder>() {
    val adArray = ArrayList<Ad>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdHolder {
        val rootElement = AdListItemBinding.inflate(LayoutInflater.from(parent.context))
        return AdHolder(rootElement, activity)
    }

    override fun onBindViewHolder(holder: AdHolder, position: Int) {
        holder.setData(adArray[position])
    }

    override fun getItemCount(): Int = adArray.size

    fun updateAdapter(newList: ArrayList<Ad>) {
        val diffResult = DiffUtil.calculateDiff(DiffUtilHelper(adArray, newList))
        diffResult.dispatchUpdatesTo(this)
        adArray.clear()
        adArray.addAll(newList)

    }

    class AdHolder(val rootElement: AdListItemBinding, val activity: MainActivity) :
        RecyclerView.ViewHolder(rootElement.root) {

        fun setData(ad: Ad) = with(rootElement) {
            textViewTitle.text = ad.title
            tvDescription.text = ad.description
            tvPrice.text = ad.price
            tvViewCounter.text = ad.viewsCounter
            tvFavCounter.text = ad.favCounter
            if (ad.isFav) {
                btnFau.setImageResource(R.drawable.ic_favarite_pressed)
            } else {
                btnFau.setImageResource(R.drawable.ic_favorite)
            }
            showEditPanel(isOwner(ad))
            btnFau.setOnClickListener {
                if (activity.mAuth.currentUser?.isAnonymous == false) activity.onFavClicked(ad)
            }
            itemView.setOnClickListener { activity.onAdViewed(ad) }
            ibEditAd.setOnClickListener(onClickEdit(ad))
            ibDeleteAd.setOnClickListener { activity.onDeleteItem(ad) }
        }

        private fun onClickEdit(ad: Ad): View.OnClickListener {
            return View.OnClickListener {
                val editIntent = Intent(activity, EditAddActivity::class.java).apply {
                    putExtra(EDIT_STATE, true)
                    putExtra(ADS_DATA, ad)
                }
                activity.startActivity(editIntent)
            }
        }

        private fun isOwner(ad: Ad): Boolean {
            return ad.uid == activity.mAuth.uid
        }

        private fun showEditPanel(isOwner: Boolean) {
            if (isOwner) {
                rootElement.editPanel.visibility = View.VISIBLE
            } else {
                rootElement.editPanel.visibility = View.GONE
            }
        }
    }

    interface ItemsListener {
        fun onDeleteItem(ad: Ad)
        fun onAdViewed(ad: Ad)
        fun onFavClicked(ad: Ad)
    }
}