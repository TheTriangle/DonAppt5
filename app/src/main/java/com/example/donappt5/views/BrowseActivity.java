package com.example.donappt5.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.donappt5.R;
import com.example.donappt5.data.util.MyGlobals;
import com.example.donappt5.views.charitylist.CharityListActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class BrowseActivity extends AppCompatActivity {
    MyGlobals myGlobals;
    Context context;
    BottomNavigationView bottomNavigationView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);
        Log.d("progresstracker", "browseactivity");
        context = this;

        myGlobals = new MyGlobals(context);;
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        myGlobals.setupBottomNavigation(context, this, bottomNavigationView);

        ConstraintLayout kids = findViewById(R.id.kids);
        ConstraintLayout poverty = findViewById(R.id.poverty);
        ConstraintLayout science_research = findViewById(R.id.science_research);
        ConstraintLayout art = findViewById(R.id.art);
        ConstraintLayout healthcare = findViewById(R.id.healthcare);
        ConstraintLayout education = findViewById(R.id.education);

        Context ctx = this;

        kids.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, CharityListActivity.class);
                intent.putExtra("tagclicked", "children");
                Log.d("progresstracker", "browseactivityendc");
                startActivity(intent);
            }
        });
        poverty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, CharityListActivity.class);
                intent.putExtra("tagclicked", "poverty");
                Log.d("progresstracker", "browseactivityendp");
                startActivity(intent);
            }
        });
        art.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, CharityListActivity.class);
                intent.putExtra("tagclicked", "art");
                Log.d("progresstracker", "browseactivityenda");
                startActivity(intent);
            }
        });
        science_research.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, CharityListActivity.class);
                intent.putExtra("tagclicked", "science&research");
                Log.d("progresstracker", "browseactivityends");
                startActivity(intent);
            }
        });
        healthcare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, CharityListActivity.class);
                intent.putExtra("tagclicked", "healthcare");
                Log.d("progresstracker", "browseactivityendh");
                startActivity(intent);
            }
        });
        education.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, CharityListActivity.class);
                intent.putExtra("tagclicked", "education");
                Log.d("progresstracker", "browseactivityende");
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        myGlobals.setSelectedItem(this, bottomNavigationView);
    }
}