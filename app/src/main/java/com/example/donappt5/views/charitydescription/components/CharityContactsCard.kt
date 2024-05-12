package com.example.donappt5.views.charitydescription.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.example.donappt5.R


@Preview
@Composable
fun CharityContactsCard(contacts: ArrayList<Map<String, String>> = arrayListOf(mapOf("16" to "example website"))) {
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .background(
                color = Color.White,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(10.dp)
            .wrapContentHeight()
    ) {
        Column(modifier = Modifier.padding(15.dp)) {
            Row {
                val websiteLinkClicked = remember { mutableStateOf(false) }
                val website = contacts.filter { contact -> contact["id"] == "16" }
                    .getOrNull(0)?.getOrDefault("value", "") ?: ""
                Icon(
                    painter = painterResource(R.drawable.website_icon),
                    contentDescription = "charity website icon"
                )
                if (website.isNotEmpty()) {
                    ClickableText(text = AnnotatedString(
                        website
                    ), onClick = {
                        websiteLinkClicked.value = true
                    })
                    if (websiteLinkClicked.value) {
                        val browserIntent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("http://" + contacts.filter { contact -> contact["id"] == "4" }[0]["value"])
                        )
                        LocalContext.current.startActivity(browserIntent)
                        websiteLinkClicked.value = false
                    }
                } else {
                    Text(stringResource(id = R.string.no_website))
                }
            }
        }
    }

}