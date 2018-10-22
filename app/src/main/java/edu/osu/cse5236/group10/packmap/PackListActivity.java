package edu.osu.cse5236.group10.packmap;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import edu.osu.cse5236.group10.packmap.data.DummyContent;

public class PackListActivity extends AppCompatActivity implements PackFragment.OnListFragmentInteractionListener {

    private static final String TAG = "PackListActivity";

    private TextView mTextMessage;
    private PackFragment mPackFragment;

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
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate() called");

        setContentView(R.layout.activity_pack_list);

        mTextMessage = (TextView) findViewById(R.id.message);
        mPackFragment = (PackFragment) getSupportFragmentManager().findFragmentById(R.id.pack_list);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }
}
