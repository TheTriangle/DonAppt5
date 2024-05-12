package com.example.donappt5.views.charitydescription.components

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.donappt5.R
import com.example.donappt5.data.model.Campaign
import com.example.donappt5.data.model.Campaign.Companion.putCampaignExtra
import com.example.donappt5.views.campaign.CampaignActivity

@Preview
@Composable
fun CampaignListItem(parentCharityName: String = "Подари Жизнь", campaign: Campaign = Campaign()) {
    val ctx = LocalContext.current
    Row(Modifier.clickable {
        val intent = Intent(ctx, CampaignActivity::class.java)
        intent.putExtra("charityname", parentCharityName)
        intent.putCampaignExtra(campaign)
        ctx.startActivity(intent)
    }) {
        if (campaign.totalAmount != 0) {
            ProgressBar(campaign.collectedAmount.toDouble() / campaign.totalAmount, campaign.name)
        }
        else {
            ProgressBar(0.0, campaign.name)
        }
        Icon(
            painter = painterResource(R.drawable.rightarrow),
            contentDescription = "proceed to charity icon"
        )
    }
}