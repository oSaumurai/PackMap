package edu.osu.cse5236.group10.packmap;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnitRunner;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class MapFragmentTest extends AndroidJUnitRunner {

    private MapsActivity mMapActivity=null;
    private MapFragment mMapFragment;
    //widget
    private AutoCompleteTextView mSearchText;
    private BottomSheetBehavior bottomSheetBehavior;
    private TextView bottom_heading;
    private ImageView locateButton;
    private Button selectButton;

    @Rule
    public ActivityTestRule<MapsActivity> mapsActivityTestRule=new ActivityTestRule<MapsActivity>(MapsActivity.class);

    @Before
    public void setUp(){
        mMapActivity=mapsActivityTestRule.getActivity();

    }

    @Test
    public void testPreconditions()

    {

    }

    @Test
    public void testLaunch(){
        ConstraintLayout clContainder=(ConstraintLayout) mMapActivity.findViewById(R.id.map_fragment_container);
        Assert.assertNotNull(clContainder);
        mMapFragment=new MapFragment();
        mMapActivity.getSupportFragmentManager().beginTransaction().add(clContainder.getId(),mMapFragment).commitAllowingStateLoss();
        //View view = mMapFragment.getView().f(R.id.)
        //Assert.assertNotNull(view);

    }

    @Test
    public void testUi(){

    }

}