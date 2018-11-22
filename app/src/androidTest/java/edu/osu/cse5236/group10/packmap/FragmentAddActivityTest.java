package edu.osu.cse5236.group10.packmap;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnitRunner;
import android.view.MotionEvent;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

public class FragmentAddActivityTest extends AndroidJUnitRunner {

    private CreateActivityActivity mCreateActivityActivity = null;
    private MapFragment mMapFragment;
    //widget
    private ConstraintLayout clContainder;
    private AutoCompleteTextView mSearchText;
    private BottomSheetBehavior bottomSheetBehavior;
    private TextView bottom_heading;
    private ImageView locateButton;
    private Button selectButton;

    @Rule
    public ActivityTestRule<CreateActivityActivity> mapsActivityTestRule=new ActivityTestRule<CreateActivityActivity>(CreateActivityActivity.class);

    @Before
    public void setUp(){
        mCreateActivityActivity =mapsActivityTestRule.getActivity();
        clContainder=(ConstraintLayout) mCreateActivityActivity.findViewById(R.id.map_fragment_container);
        mMapFragment=new MapFragment();
        mCreateActivityActivity.getSupportFragmentManager().beginTransaction().add(clContainder.getId(),mMapFragment).commit();
    }

    @Test
    public void testPreconditions()
    {
        Assert.assertNotNull(mCreateActivityActivity);
        Assert.assertNotNull(mMapFragment);
        Assert.assertNotNull(clContainder);
    }

    @Test
    public void testUI(){
        System.out.println("Thread ID in testUI:" + Thread.currentThread().getId());
        getInstrumentation().waitForIdleSync();

        mCreateActivityActivity.runOnUiThread(new Runnable() {
            public void run() {
                System.out.println("Thread ID in TestUI.run:" + Thread.currentThread().getId());

                //mBoard.requestFocus();
                MotionEvent newMotionEvent = MotionEvent.obtain((long)1,
                        (long)1,
                        MotionEvent.ACTION_DOWN,
                        (float) 53.0,
                        (float) 53.0,
                        0);
                //mBoard.dispatchTouchEvent(newMotionEvent);
                //mMapFragment.scheduleAndroidsTurn();
                //assertEquals(mGameSessionFragment.getPlayCount(), 0);
            }
        });
    }

    @Test
    public void testLaunch(){


    }


}