package com.example.donappt5.data.util

import android.content.Context
import android.content.res.Resources
import com.example.donappt5.R

class TagConverter {
    companion object {
        private lateinit var tagToStringMap: Map<Int, String>
        fun transposeTag(tag: Int): String {
            if (!this::tagToStringMap.isInitialized) return "tags not initialized"
            return tagToStringMap[tag]?: "unknown tag"
        }

        fun init(context: Context) {
            tagToStringMap = mapOf(1 to context.resources.getString(R.string.kids_tag),
                2 to context.resources.getString(R.string.families_tag),
                3 to context.resources.getString(R.string.animals_tag),
                4 to context.resources.getString(R.string.homeless_tag),
                5 to context.resources.getString(R.string.healthcare_tag),
                6 to context.resources.getString(R.string.poverty_tag))
        }
    }
}