package com.example.donappt5.views.charitydescription.components

import android.content.Intent
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.donappt5.data.model.Campaign
import com.example.donappt5.data.model.Campaign.Companion.toCampaign
import com.example.donappt5.data.services.FirestoreService
import com.example.donappt5.views.campaign.CampaignActivity

@Preview
@Composable
fun CampaignsList(charityName: String = "Подари жизнь", charityId: String = "00GLN0wFyNoh2cSnDHaYaKBA1GMn") {
    var campaigns by remember {
        mutableStateOf(arrayListOf<Campaign>())
    }
    LazyColumn {
        items(campaigns) {
            for (campaign in campaigns) {
                CampaignListItem(charityName, campaign)
            }
        }
    }
    FirestoreService.getCampaignsFor(charityId).addOnCompleteListener {
        for (doc in it.result) {
            val loadedCampaigns = arrayListOf<Campaign>()
            doc.toCampaign()?.let { campaign -> loadedCampaigns.add(campaign) }
            campaigns = loadedCampaigns
        }
    }
}