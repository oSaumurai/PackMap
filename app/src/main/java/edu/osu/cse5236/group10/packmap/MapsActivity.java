package edu.osu.cse5236.group10.packmap;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;


public class MapsActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.map_fragment_container);

        if (fragment == null) {
            fragment = new MapFragment();
            fm.beginTransaction()
                    .add(R.id.map_fragment_container, fragment)
                    .commit();
        }
    }

}
