package com.example.donappt5.views.charitydescription.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Preview
@Composable
fun ProgressBar(progress: Double = 0.5, overlayText: String = "Placeholder") {
    Box(contentAlignment = Alignment.CenterStart, modifier = Modifier.fillMaxSize()) {
        if (progress < 0.999) {
            Box(
                contentAlignment = Alignment.CenterStart,
                modifier = Modifier
                    .background(
                        color = Color(0xFF97C3F3),
                        shape = RoundedCornerShape(50.dp)
                    )
                    .border(
                        border = BorderStroke(5.dp, Color(0xFF97C3F3)),
                        shape = RoundedCornerShape(50.dp)
                    )
                    .height(55.dp)
                    .fillMaxWidth()
                    .padding(5.dp)
            ) {
            }
        } else {
            Box(
                modifier = Modifier
                    .background(
                        color = Color(0xFF67AB8D),
                        shape = RoundedCornerShape(55.dp)
                    )
                    .fillMaxWidth(progress.toFloat())
                    .height(55.dp)
            ) {
            }
        }
        Text(overlayText, modifier = Modifier.padding(15.dp), fontSize = 15.sp)
    }
}