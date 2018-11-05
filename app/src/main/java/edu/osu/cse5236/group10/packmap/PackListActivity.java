package edu.osu.cse5236.group10.packmap;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import java.util.HashMap;
import java.util.Map;

import edu.osu.cse5236.group10.packmap.data.AddUserListContent;
import edu.osu.cse5236.group10.packmap.data.PackListContent;
import edu.osu.cse5236.group10.packmap.data.store.UserStore;

public class PackListActivity extends AppCompatActivity implements
        PackFragment.OnPackListFragmentInteractionListener,
        AddToPackFragment.OnAddUserListFragmentInteractionListener {

    private static final String TAG = "PackListActivity";

    private TextView mTextMessage;
    private PackFragment mPackFragment;
    private FloatingActionButton mAddButton;
    private String mPhoneNum;
    private int currNavigateId;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();

            if (id != currNavigateId)
                switch (id) {
                    case R.id.navigation_home:
                        transaction.replace(R.id.pack_list_container, new PackFragment());
                        transaction.commit();
                        currNavigateId = R.id.navigation_home;
                        return true;
                    case R.id.navigation_dashboard:
                        transaction.replace(R.id.pack_list_container, new MapFragment());
                        transaction.commit();
                        currNavigateId = R.id.navigation_dashboard;
                        mAddButton.hide();
                        Log.d(TAG, "triggered");
                        return true;
                    case R.id.navigation_notifications:
                        currNavigateId = R.id.navigation_notifications;
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
        Intent intent;

        switch (id) {
            case R.id.create_group:
                intent = new Intent(this, CreateGroupActivity.class);
                intent.putExtra("userId", mPhoneNum);
                startActivity(intent);
                return true;
            case R.id.modify_account:
                intent = new Intent(this, AccountSettingActivity.class);
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
        Log.d(TAG, mPhoneNum);

        // Get FAB button
        mAddButton = findViewById(R.id.add_fab);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                Fragment fragment = fm.findFragmentById(R.id.add_to_pack_layout);
                if (fragment == null) {
                    fragment = new AddToPackFragment();
                }
                mAddButton.hide();
                fm.beginTransaction()
                            .replace(R.id.pack_list_container, fragment)
                            .commit();

            }
        });

        // Add bottom navigation bar
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Add fragment to container
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.pack_list_container);

        currNavigateId = R.id.navigation_home;

        if (fragment == null) {
            fm.beginTransaction()
                    .add(R.id.pack_list_container, new PackFragment())
                    .commit();
        }
    }

    public FloatingActionButton getAddButton() {
        return mAddButton;
    }

    @Override
    public void onListFragmentInteraction(PackListContent.PackItem item) {

    }

    @Override
    public void onListFragmentInteraction(AddUserListContent.AddUserItem item) {

    }
}
