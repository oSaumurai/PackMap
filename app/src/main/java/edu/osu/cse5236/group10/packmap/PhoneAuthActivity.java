package edu.osu.cse5236.group10.packmap;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PhoneAuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.phone_auth_fragment_container);

        if (fragment == null) {
            fragment = new PhoneAuthFragment();
            fm.beginTransaction()
                    .add(R.id.phone_auth_fragment_container, fragment)
                    .commit();
        }
    }
}
