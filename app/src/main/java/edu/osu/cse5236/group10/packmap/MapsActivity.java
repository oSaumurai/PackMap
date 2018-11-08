package edu.osu.cse5236.group10.packmap;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;


public class MapsActivity extends FragmentActivity {
    private static final String TAG = "MapsActivity";

    private FloatingActionButton mAddButton;
    private String mGroupId;
    private String mActivityId;
    private String userId;

    private int currNavigateId;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            Fragment fragment;

            if (id != currNavigateId)
                switch (id) {
                    case R.id.navigation_activity_map:
                        fragment = new MapFragment();
                        fragment.setArguments(getIntent().getExtras());
                        transaction.replace(R.id.map_fragment_container, fragment);
                        transaction.commit();
                        currNavigateId = R.id.navigation_activity_map;
                        return true;

                    case R.id.navigation_activity_vote:
                        //fragment = new LocationListFragment();
                        //fragment.setArguments(getIntent().getExtras());
                        //transaction.replace(R.id.map_fragment_container, fragment);
                        transaction.commit();
                        currNavigateId = R.id.navigation_activity_vote;
                        return true;
                }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        mGroupId=this.getIntent().getStringExtra("groupId");
        mActivityId=this.getIntent().getStringExtra("activityId");
        userId=this.getIntent().getStringExtra("userId");

        Log.d(TAG, userId + " - " + mGroupId + " - " + mActivityId);

        currNavigateId = R.id.navigation_activity_map;
        BottomNavigationView navigation = findViewById(R.id.navigation_activity);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.map_fragment_container);

        if (fragment == null) {
            fragment = new MapFragment();
            fm.beginTransaction()
                    .add(R.id.map_fragment_container, fragment)
                    .commit();
        }

        mAddButton = findViewById(R.id.add_fab);
    }
}
