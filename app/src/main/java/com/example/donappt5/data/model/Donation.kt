package com.example.donappt5.data.model

import android.util.Log
import com.example.donappt5.R
import com.google.firebase.firestore.DocumentSnapshot
import java.util.Date

class Donation {
    var id: String? = null
    var time: Date? = null
    var charityId: String? = null
    var charityName: String? = null
    var campaignId: String? = null
    var campaignName: String? = null
    var amount: Double? = 0.0
    var currency: String? = null

    constructor(document: DocumentSnapshot) {
        id = document.id
        charityName = document.getString("charityname")?: "No name"
        charityId = document.getString("charityid")?: "No name"
        campaignName = document.getString("campaignname")
        campaignId = document.getString("campaignid")

        amount = document.getDouble("amount")?: 0.0
        currency = document.getString("currency")
        time = document.getDate("date")
    }

    companion object {
        fun DocumentSnapshot.toDonation(): Donation? {
            try {
                return Donation(this)
            } catch (e: Exception) {
                Log.e(TAG, "Error converting charity", e)
                return null
            }
        }
        private const val TAG = "DonationClass"
    }
}