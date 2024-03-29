package edu.osu.cse5236.group10.packmap;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class AccountSettingActivity extends AppCompatActivity {
    private final String TAG = "AccountSettingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_account_setting);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.account_setting_fragment_container);

        if (fragment == null) {
            fragment = new AccountSettingFragment();

            fragment.setArguments(getIntent().getExtras());
            fm.beginTransaction()
                    .add(R.id.account_setting_fragment_container, fragment)
                    .commit();
        }
    }
}
