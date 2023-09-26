package com.example.donappt5.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.drawerlayout.widget.DrawerLayout
import com.example.donappt5.*
import com.example.donappt5.views.*
import com.example.donappt5.views.charitycreation.CharityCreationActivity
import com.example.donappt5.views.charitydescription.CharityActivity
import com.example.donappt5.views.charitylist.CharityListActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

class MyGlobals     // constructor
    (var mContext: Context) {
    var drawerlayout: DrawerLayout? = null

    fun setupBottomNavigation(
        ctx: Context,
        activity: Activity,
        view: BottomNavigationView
    ) {
        view.labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_UNLABELED;

        activity.overridePendingTransition(0,0)

        setSelectedItem(activity, view)

        view.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.charitylist -> {
                    val intent5 = Intent(ctx, CharityListActivity::class.java)
                    activity.startActivity(intent5)
                    true
                }
                R.id.map -> {
                    val intent3 = Intent(ctx, CharitiesMapActivity::class.java)
                    activity.startActivity(intent3)
                    true
                }
                R.id.donations -> {
                    val intent6 = Intent(ctx, OwnedCharityListActivity::class.java)
                    activity.startActivity(intent6)
                    true
                }
                R.id.profile -> {
                    val intent = Intent(ctx, ProfileActivity::class.java)
                    activity.startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

    fun setSelectedItem(activity: Activity,
        view: BottomNavigationView) {
        view.menu.findItem(when(activity) {
            is CharityListActivity -> { R.id.charitylist }
            // is BrowseActivity -> { R.id.charitylist }
            is CharitiesMapActivity -> { R.id.map }
            is CharityActivity -> { R.id.charitylist }
            is CharityCreationActivity -> { R.id.donations }
            is CharityEditActivity -> { R.id.donations }
            is OwnedCharityListActivity -> { R.id.donations }
            is ProfileActivity -> { R.id.profile }
            is SettingsActivity -> { R.id.profile }
            else -> R.id.charitylist
        }).isChecked = true
    }
}