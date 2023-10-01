package com.example.donappt5.data.util

import android.R.attr.bitmap
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.example.donappt5.data.model.MyClusterItem
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer


class MyClusterRenderer(
    context: Context?, map: GoogleMap?,
    clusterManager: ClusterManager<MyClusterItem?>?
) : DefaultClusterRenderer<MyClusterItem?>(context, map, clusterManager) {
    override fun getColor(clusterSize: Int): Int {
        return Color.parseColor("#000000")
    }

    override fun onBeforeClusterItemRendered(
        item: MyClusterItem,
        markerOptions: MarkerOptions
    ) {
        if (item.isCluster) {
            val clusterIcon = BitmapDescriptorFactory
                .fromBitmap(createMarkerImage(120, 120, item.itemsAmount.toString()))
            markerOptions.icon(clusterIcon)
        } else {
            val markerIcon =
                BitmapDescriptorFactory.defaultMarker(0.0f) //TODO stylize to black
            markerOptions.icon(markerIcon)
        }
    }

    fun createMarkerImage(width: Int, height: Int, name: String): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint2 = Paint()
        paint2.setColor(Color.BLACK)
        canvas.drawCircle(width.toFloat()/2, height.toFloat()/2, width.toFloat()/2, paint2)
        val paint = Paint()
        paint.setColor(Color.WHITE)
        paint.setTextSize(42F)
        canvas.drawText(name, 10f, height.toFloat()/2 + 15, paint)
        return bitmap
    }
}