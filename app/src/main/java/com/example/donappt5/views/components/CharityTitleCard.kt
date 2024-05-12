package com.example.donappt5.views.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.donappt5.R
import com.example.donappt5.databinding.ComponentCharityTitleCardBinding


class CharityTitleCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    lateinit var binding: ComponentCharityTitleCardBinding
    init {
        //for preview
        if (isInEditMode) {
            LayoutInflater.from(context).inflate(R.layout.component_charity_title_card, this, true)
        } else {
            binding = ComponentCharityTitleCardBinding.inflate(LayoutInflater.from(getContext()))
            addView(binding.root)
        }
    }
}