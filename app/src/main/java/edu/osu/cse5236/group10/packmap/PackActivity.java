package edu.osu.cse5236.group10.packmap;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pack);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.pack_fragment_container);

        if (fragment == null) {
            fragment = new PackFragment();
            fm.beginTransaction()
                    .add(R.id.pack_fragment_container, fragment)
                    .commit();
        }
    }
}
