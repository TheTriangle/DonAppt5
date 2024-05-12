package com.example.donappt5.views.campaign.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.donappt5.R
import com.example.donappt5.views.CustomTheme
import com.example.donappt5.views.charitydescription.components.ProgressBar


@Preview
@Composable
fun CampaignTitleCard(charityName: String = "Регион Заботы",
                      campaignName: String = "Ремонт школы 57", totalAmount: Int = 10000,
                      collectedAmount: Int = 2000) {
    CustomTheme {
        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier
                .background(
                    color = CustomTheme.colors.surface,
                    shape = RoundedCornerShape(10.dp)
                )
                .wrapContentHeight()
                .shadow(CustomTheme.elevation.default)
                .padding(15.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    charityName,
                    style = CustomTheme.typography.header,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center
                )
                Text(
                    campaignName,
                    style = CustomTheme.typography.title,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center
                )
                if (totalAmount > 0) ProgressBar(collectedAmount.toDouble() / totalAmount, "")
                else ProgressBar(0.0, "")
                Text(
                    color = Color(0xFF67AB8D), text = stringResource(
                        R.string.collected_out_of_text,
                        collectedAmount.toString(), totalAmount.toString() + "₽"
                    ),
                    style = CustomTheme.typography.body
                )
            }
        }
    }
}