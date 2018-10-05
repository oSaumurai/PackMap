package edu.osu.cse5236.group10.packmap;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import edu.osu.cse5236.group10.packmap.dummy.DummyContent;

public class PackListActivity extends AppCompatActivity implements PackFragment.OnListFragmentInteractionListener {

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
