package edu.osu.cse5236.group10.packmap;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import edu.osu.cse5236.group10.packmap.data.PackListContent;
import edu.osu.cse5236.group10.packmap.data.store.UserStore;

public class PackListActivity extends AppCompatActivity implements PackFragment.OnListFragmentInteractionListener {

    private static final String TAG = "PackListActivity";

    private TextView mTextMessage;
    private PackFragment mPackFragment;
    private String mPhoneNum;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_packs);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_places);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_explore);
                    return true;
            }
            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.pack_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.modify_account:
                Intent intent = new Intent(this, AccountSettingActivity.class);
                intent.putExtra("userId", mPhoneNum);

                Log.d(TAG, "Sending: " + mPhoneNum);
                startActivity(intent);
                return true;
            case R.id.delete_account:
                UserStore.getInstance().deleteUserById(mPhoneNum);

                finish();
                return true;
        }

        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate() called");

        setContentView(R.layout.activity_pack_list);

        mTextMessage = findViewById(R.id.message);

        mPhoneNum = getIntent().getStringExtra("userId");

        // Add bottom navigation bar
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Add fragment to container
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.pack_list_container);

        if (fragment == null) {
            fragment = new PackFragment();

            fm.beginTransaction()
                    .add(R.id.pack_list_container, fragment)
                    .commit();
        }
    }

    @Override
    public void onListFragmentInteraction(PackListContent.PackItem item) {

    }
}
