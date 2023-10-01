package com.example.donappt5.data.model

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class MyClusterItem(
    lat: Double,
    lng: Double,
    private val mTitle: String,
    private val mSnippet: String,
    val id: String,
    val isCluster: Boolean,
    val itemsAmount: Int,
) :
    ClusterItem {
    private val mPosition: LatLng

    init {
        mPosition = LatLng(lat, lng)
    }

    override fun getPosition(): LatLng {
        return mPosition
    }

    override fun getTitle(): String {
        return mTitle
    }

    override fun getSnippet(): String {
        return mSnippet
    }
}