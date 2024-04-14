package com.example.donappt5.views.charitylist

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.CheckBox
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.donappt5.R
import com.example.donappt5.viewmodels.CharityListViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton


class SearchDialogFragment(var radius: Int) : BottomSheetDialogFragment() {
    val viewModel: CharityListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.search_bottom_sheet, container, false)
        val rootLayout = view.findViewById<ConstraintLayout>(R.id.rootLayout)
        val rectangle = rootLayout.background as GradientDrawable
        rectangle.cornerRadius = radius.toFloat()

        // Set window flags to remove dimming effect and make dialog appear on top of activity
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.CustomBottomSheetDialog);
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog?.window?.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        dialog?.window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);

        val fabSendSearch = rootLayout.findViewById<FloatingActionButton>(R.id.fabSendSearch)
        val tagKids = rootLayout.findViewById<CheckBox>(R.id.tagKids)
        val tagHealthcare = rootLayout.findViewById<CheckBox>(R.id.tagHealthcare)
        val tagEducation = rootLayout.findViewById<CheckBox>(R.id.tagEducation)
        val tagPoverty = rootLayout.findViewById<CheckBox>(R.id.tagPoverty)
        val tagArt = rootLayout.findViewById<CheckBox>(R.id.tagArt)
        val tagScience = rootLayout.findViewById<CheckBox>(R.id.tagScience)
        val searchBar = rootLayout.findViewById<EditText>(R.id.search_bar)
        fabSendSearch.setOnClickListener {
            viewModel.parseSearchInfo(tagKids.isChecked, tagPoverty.isChecked,
            tagHealthcare.isChecked, tagScience.isChecked, tagArt.isChecked,
            tagEducation.isChecked, searchBar.text.toString())
        }
        return view
    }
}