package com.russellworld.russellboard.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.russellworld.russellboard.R
import com.russellworld.russellboard.utilits.CityHelper

class DialogSpinnerHelper {
    fun showSpinnerDialog(context: Context, list: ArrayList<String>) {
        val builder = AlertDialog.Builder(context)
        val dialog = builder.create()
        val rootElement = LayoutInflater.from(context).inflate(R.layout.spinner_layout, null)
        val adapter = RcDialogSpinnerAdapter(context, dialog)
        val rcView = rootElement.findViewById<RecyclerView>(R.id.rcSpiinnerView)
        val searchView = rootElement.findViewById<SearchView>(R.id.svSpinner)
        rcView.layoutManager = LinearLayoutManager(context)
        rcView.adapter = adapter
        dialog.setView(rootElement)
        adapter.updateAdapter(list)
        setSearchView(adapter, list, searchView)
        dialog.show()
    }

    private fun setSearchView(
        adapter: RcDialogSpinnerAdapter,
        list: ArrayList<String>,
        searchView: SearchView?
    ) {
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val tempList = CityHelper.filterListData(list, newText)
                adapter.updateAdapter(tempList)
               return true
            }
        })
    }

}