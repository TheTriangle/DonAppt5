package com.example.donappt5.views.components

import android.content.Context
import android.content.res.Resources
import android.nfc.Tag
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.getString
import com.example.donappt5.R
import com.example.donappt5.data.util.TagConverter.Companion.transposeTag
import com.example.donappt5.databinding.ComponentTagsListBinding



@Preview
@Composable
fun TagItem(tag: String = "preview tag") {
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .background(
                color = Color.Transparent,
                shape = RoundedCornerShape(5.dp)
            )
            .border(
                border = BorderStroke(1.dp, Color(0xFF67AB8D)),
                shape = RoundedCornerShape(5.dp)
            )
            .padding(5.dp)
            .wrapContentHeight()
    ) {
        Text(transposeTag(tag.toInt()), fontSize = 15.sp)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Preview
@Composable
fun TagList(tagsList: List<String> = listOf("1", "2", "3")) {
    FlowRow(
        modifier = Modifier.padding(4.dp),
    ) {
        for (tag in tagsList) {
            TagItem(tag)
        }
    }
}