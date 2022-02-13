package com.russellworld.russellboard.fragments

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.russellworld.russellboard.databinding.FragmentListImageBinding

open class BaseSelectImageFragment : Fragment() {
    lateinit var rootElement: FragmentListImageBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootElement = FragmentListImageBinding.inflate(layoutInflater)
        return rootElement.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initAds()
    }

    override fun onResume() {
        super.onResume()
        rootElement.adView.resume()
    }

    override fun onPause() {
        super.onPause()
        rootElement.adView.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        rootElement.adView.destroy()
    }

    private fun initAds() {
        MobileAds.initialize(activity as Activity)
        val adRequest = AdRequest.Builder().build()
        rootElement.adView.loadAd(adRequest)
    }
}