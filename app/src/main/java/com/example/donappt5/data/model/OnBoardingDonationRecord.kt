package com.example.donappt5.data.model

import kotlinx.serialization.Serializable

@Serializable
data class OnBoardingDonationRecord(
    val charityName: String,
    val monthlyDonation: Int?
)
