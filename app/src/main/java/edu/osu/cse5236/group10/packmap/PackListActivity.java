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

import edu.osu.cse5236.group10.packmap.data.AddUserListContent;
import edu.osu.cse5236.group10.packmap.data.PackListContent;
import edu.osu.cse5236.group10.packmap.data.store.UserStore;

public class PackListActivity extends AppCompatActivity implements
        PackListFragment.OnPackListFragmentInteractionListener {

    private static final String TAG = "PackListActivity";

    private TextView mTextMessage;
    private PackListFragment mPackListFragment;
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
                    case R.id.navigation_packs:
                        transaction.replace(R.id.pack_list_container, new PackListFragment());
                        transaction.commit();
                        currNavigateId = R.id.navigation_packs;
                        return true;
                    case R.id.navigation_places:
                        transaction.replace(R.id.pack_list_container, new MapFragment());
                        transaction.commit();
                        currNavigateId = R.id.navigation_places;
                        mAddButton.hide();
                        Log.d(TAG, "triggered");
                        return true;
                    case R.id.navigation_explore:
                        currNavigateId = R.id.navigation_explore;
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
            case R.id.logout_account:
                UserSharedPreference.setLoggedIn(this, false, null);
                intent = new Intent(this, LogInSignUpActivity.class);

                Log.d(TAG, "Logging out: " + mPhoneNum);
                startActivity(intent);
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
        PackListActivity thisActivity = this;
        mAddButton = findViewById(R.id.add_fab);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAddButton.hide();
                Intent intent = new Intent(thisActivity, CreateGroupActivity.class);
                intent.putExtra("userId", mPhoneNum);
                startActivity(intent);
            }
        });

        // Add bottom navigation bar
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Add fragment to container
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.pack_list_container);

        currNavigateId = R.id.navigation_packs;

        if (fragment == null) {
            fm.beginTransaction()
                    .add(R.id.pack_list_container, new PackListFragment())
                    .commit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        switch (currNavigateId) {
            case R.id.navigation_packs:
                transaction.replace(R.id.pack_list_container, new PackListFragment());
                transaction.commit();
                mAddButton.show();
                currNavigateId = R.id.navigation_packs;
                break;
            case R.id.navigation_places:
                transaction.replace(R.id.pack_list_container, new MapFragment());
                transaction.commit();
                currNavigateId = R.id.navigation_places;
                mAddButton.hide();
                Log.d(TAG, "triggered");
                break;
            case R.id.navigation_explore:
                currNavigateId = R.id.navigation_explore;
                mAddButton.hide();
                break;
        }
    }

    public FloatingActionButton getAddButton() {
        return mAddButton;
    }

    @Override
    public void onListFragmentInteraction(PackListContent.PackItem item) {
        Intent intent = new Intent(this, PackActivity.class);
        intent.putExtra("userId", mPhoneNum);
        intent.putExtra("groupId", item.group.getDocumentId());
        startActivity(intent);
    }
}
