package edu.osu.cse5236.group10.packmap;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import edu.osu.cse5236.group10.packmap.data.model.ActivityInfo;

public class PackActivity extends AppCompatActivity implements PackFragment.OnPackFragmentInteractionListener{

    private int currNavigateId;
    private String mPhoneNum;
    private String GroupId;

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
                    case R.id.navigation_pack_activities:
                        fragment = new PackFragment();
                        fragment.setArguments(getIntent().getExtras());
                        transaction.replace(R.id.pack_fragment_container, fragment);
                        transaction.commit();
                        currNavigateId = R.id.navigation_pack_activities;
                        return true;

                    case R.id.navigation_pack_members:
                        fragment = new PackMemberFragment();
                        fragment.setArguments(getIntent().getExtras());
                        transaction.replace(R.id.pack_fragment_container, fragment);
                        transaction.commit();
                        currNavigateId = R.id.navigation_pack_members;
                        return true;
                }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pack);

        GroupId=this.getIntent().getStringExtra("groupId");
        mPhoneNum=this.getIntent().getStringExtra("userId");

        currNavigateId = R.id.navigation_pack_activities;
        BottomNavigationView navigation = findViewById(R.id.navigation_packs);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.pack_fragment_container);

        if (fragment == null) {
            fragment = new PackFragment();
            fragment.setArguments(getIntent().getExtras());
            fm.beginTransaction()
                    .add(R.id.pack_fragment_container, fragment)
                    .commit();
        }
    }

    @Override
    public void onListFragmentInteraction(String item) {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("userId", mPhoneNum);
        intent.putExtra("groupId", GroupId);
        startActivity(intent);
    }

}
