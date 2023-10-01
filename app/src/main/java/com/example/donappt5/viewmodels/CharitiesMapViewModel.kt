package com.example.donappt5.viewmodels

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Looper
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.donappt5.data.model.MyClusterItem
import com.example.donappt5.data.services.FirestoreService
import com.example.donappt5.data.util.Util
import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import com.firebase.geofire.GeoQueryBounds
import com.firebase.geofire.core.GeoHash
import com.google.android.gms.location.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.AggregateQuerySnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.maps.android.clustering.ClusterManager
import java.lang.Double.max
import kotlin.math.abs


class CharitiesMapViewModel() : ViewModel() {
    private val earthCircumferenceMeters = 40075000.0
    var pixelDensityDpi: Double? = null
    var mClusterManager = MutableLiveData<ClusterManager<MyClusterItem?>>()
    var location = MutableLiveData<LatLng>()
    var latitude = MutableLiveData<Double>()
    var longitude = MutableLiveData<Double>()
    var mFusedLocationClient = MutableLiveData<FusedLocationProviderClient>()
    var previousZoom = 0.0
    var clustersCollection = hashSetOf<MyClusterItem?>()
    var markerCollection = hashSetOf<MyClusterItem?>()

    init {
        latitude.value = 1000.0
        longitude.value = 1000.0
        location.value = LatLng(latitude.value!!, longitude.value!!)
    }

    fun getPixelsPerMeter(lat: Double, zoom: Float): Double {
        if (pixelDensityDpi == null) return 0.1
        val pixelsPerTile: Double =
            256 * (pixelDensityDpi!! / 160)
        val numTiles = Math.pow(2.0, zoom.toDouble())
        val metersPerTile = Math.cos(Math.toRadians(lat)) * earthCircumferenceMeters / numTiles
        return pixelsPerTile / metersPerTile
    }

    fun getShownMarkersCount(gmap: GoogleMap?): Int {
        var ans = 0
        if (mClusterManager.value?.markerCollection == null) {
            return 0
        }

        for (item in clustersCollection) {
            if (item?.position?.let { gmap?.projection?.visibleRegion?.latLngBounds?.contains(it) } == true) {
                ans++
            }
        }
        return ans
    }

    fun clearClusters() {
        mClusterManager.value?.removeItems(clustersCollection)
        clustersCollection.clear()
    }

    fun onMapMoved(gmap: GoogleMap) {
        if (abs(gmap.cameraPosition.zoom - previousZoom) > 0.0001) {
            clearClusters()
        }

        if (getShownMarkersCount(gmap) >= 3) return
        val curpos = gmap.cameraPosition.target
        val metersPerPx: Double =
            1 / getPixelsPerMeter(curpos.latitude, gmap.cameraPosition.zoom)
        val width = Resources.getSystem().displayMetrics.widthPixels.toDouble()
        val height = Resources.getSystem().displayMetrics.heightPixels.toDouble()
        val radiusInM = max(width, height) * metersPerPx

        //gmap.clear()
        //gmap.addCircle(CircleOptions().center(curpos).radius(radiusInM/4))

        Log.d("MapInfo", "Current zoom level: " + gmap.cameraPosition.zoom)
        parseRegion(Util.getAveragePosition(curpos, gmap.projection.visibleRegion.farLeft),
            radiusInM/4)
        parseRegion(Util.getAveragePosition(curpos, gmap.projection.visibleRegion.farRight),
            radiusInM/4)
        parseRegion(Util.getAveragePosition(curpos, gmap.projection.visibleRegion.nearLeft),
            radiusInM/4)
        parseRegion(Util.getAveragePosition(curpos, gmap.projection.visibleRegion.nearRight),
            radiusInM/4) // */
        parseRegion(curpos,
            radiusInM/4)
    }

    fun parseRegion(center: LatLng, radius: Double) {
        val centerGeoLocation = GeoLocation(center.latitude, center.longitude)

        val bounds = GeoFireUtils.getGeoHashQueryBounds(centerGeoLocation, radius)
        for (bound in bounds) {
            FirestoreService.getGeoQueryCount(bound.startHash, bound.endHash)
                .addOnCompleteListener { task ->
                    addMarkersBasedOnCount(bound, task.result)
                }
        }
    }

    fun addMarkersBasedOnCount(bound: GeoQueryBounds, querySnapshot: AggregateQuerySnapshot) {
        if (querySnapshot.count > 30) {
            FirestoreService.getLocationAfter(Util.getLexographicalAverage(
                bound.startHash.lowercase().replace("~", "z"),
                bound.endHash.lowercase().replace("~", "z")))
                .addOnCompleteListener { task ->
                    val boundMiddle = task
                        .result?.documents?.get(0)?.get("l") as ArrayList<Double>

                    Log.d("MapInfo", "Populate map: cluster - " + querySnapshot.count + " " + boundMiddle)
                    val newItem = MyClusterItem(boundMiddle[0], boundMiddle[1],
                        "Cluster of ${querySnapshot.count} charities",
                        "Zoom in to see individual charities",
                        "cluster${querySnapshot.count}", true, querySnapshot.count.toInt())
                    clustersCollection.add(newItem)
                    mClusterManager.value?.addItem(newItem)
                    mClusterManager.value?.cluster()
                }
        } else {
            addMarkersForQuery(bound)
        }
        mClusterManager.value?.cluster()
    }

    fun addMarkersForQuery(bound: GeoQueryBounds) {
        FirestoreService.getGeoQueryResults(bound.startHash, bound.endHash)
            .addOnCompleteListener { task ->
            val snap = task.result
            for (doc in snap!!.documents) {
                val coords = doc.get("l") as ArrayList<Double>
                Log.d("MapInfo", "Populate map: " + doc.id + " " + doc.get("l"))
                doc.getString("charityid")?.let {
                    val newItem = MyClusterItem(coords[0], coords[1], it, it,
                            it, false, task.result.count())
                    if (!markerCollection.contains(newItem)) {
                        markerCollection.add(newItem)
                        mClusterManager.value?.addItem(newItem)
                    }
                }
                mClusterManager.value?.cluster()
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun getLastLocation() {
        mFusedLocationClient.value?.lastLocation?.addOnCompleteListener { task ->
            val location = task.result
            if (location == null) {
                requestNewLocationData()
            } else {
                Log.d(
                    "LocatorActivityTracker",
                    "getlastlocation: lat, long: " + location.latitude + location.longitude
                )
                this.location.value = LatLng(location.latitude, location.longitude)
                latitude.value = location.latitude
                longitude.value = location.longitude
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun requestNewLocationData() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1
        mFusedLocationClient.value!!.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }


    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation = locationResult.lastLocation
            location.value = LatLng(
                mLastLocation!!.latitude, mLastLocation.longitude
            )
            latitude.value = mLastLocation.latitude
            longitude.value = mLastLocation.longitude
        }
    }
}
