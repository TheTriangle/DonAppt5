package com.example.donappt5.viewmodels

import android.util.Log
import android.widget.AbsListView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.donappt5.R
import com.example.donappt5.data.model.Charity
import com.example.donappt5.data.model.Charity.Companion.toCharity
import com.example.donappt5.data.model.Donation
import com.example.donappt5.data.model.Donation.Companion.toDonation
import com.example.donappt5.data.services.FirestoreService
import com.example.donappt5.data.util.Response
import com.example.donappt5.views.adapters.DonationsAdapter
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.DocumentSnapshot

class DonationsListViewModel : ViewModel()  {
    var secondLastItemIndex = 0
    var lastVisible = MutableLiveData<DocumentSnapshot?>()
    var donations = MutableLiveData<Response<ArrayList<Donation>>>()

    fun fillData() {
        Log.d("listfrag", "filling started")

        lastVisible.value?.let {
            FirestoreService.getDonationsList(it)
                ?.addOnSuccessListener(OnSuccessListener { documentSnapshots ->

                    if (documentSnapshots.size() == 0) return@OnSuccessListener
                    lastVisible.value = documentSnapshots.documents[documentSnapshots.size() - 1]
                    val list = arrayListOf<Donation>()
                    for (document in documentSnapshots) {
                        document.toDonation()?.let { list.add(it) }
                    }
                })
        } ?: run {
            FirestoreService.getDonationsList(null)
                ?.addOnSuccessListener { documentSnapshots ->
                    if (documentSnapshots.size() == 0) {
                        return@addOnSuccessListener
                    }
                    lastVisible.value = documentSnapshots.documents[documentSnapshots.size() - 1]
                    val list = arrayListOf<Donation>()
                    for (document in documentSnapshots) {
                        document.toDonation()?.let { list.add(it) }
                    }
                    donations.postValue(Response.success(list))
                }?.addOnFailureListener {
                    Log.d("DonationsList", "failure getting donations list: " + it.message)
                }
        }
    }

    fun onScroll(view: AbsListView, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
        when (view.id) {
            R.id.lvDonations -> {
                val lastItem = firstVisibleItem + visibleItemCount
                if (lastItem == totalItemCount) {
                    if (secondLastItemIndex != lastItem) {
                        //to avoid multiple calls for last item
                        Log.d("Last", "Last")
                        secondLastItemIndex = lastItem
                        fillData()
                    }
                }
            }
        }
    }
}