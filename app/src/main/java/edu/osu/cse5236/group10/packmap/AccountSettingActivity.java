package edu.osu.cse5236.group10.packmap;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class AccountSettingActivity extends AppCompatActivity {
    private final String TAG = "AccountSettingActivity";
    private static final String EXTRA_PHONE = "userId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String phone = getIntent().getStringExtra("userId");
        Bundle bundle = new Bundle();

        Log.d(TAG, "get: " + phone);

        setContentView(R.layout.activity_account_setting);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.account_setting_fragment_container);

        if (fragment == null) {
            fragment = new AccountSettingFragment();

            bundle.putString(EXTRA_PHONE, phone);
            fragment.setArguments(bundle);
            fm.beginTransaction()
                    .add(R.id.account_setting_fragment_container, fragment)
                    .commit();
        }
    }
}
