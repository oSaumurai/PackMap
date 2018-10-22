package edu.osu.cse5236.group10.packmap;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.sign_up_fragment_container);

        if (fragment == null) {
            fragment = new SignUpFragment();
            fm.beginTransaction()
                    .add(R.id.sign_up_fragment_container, fragment)
                    .commit();
        }
    }
}
