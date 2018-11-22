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

import com.google.firebase.firestore.GeoPoint;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import edu.osu.cse5236.group10.packmap.data.model.LocationInfo;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

public class LocationListTest extends AndroidJUnitRunner {

    private MapsActivity mMapActivity=null;
    private LocationListFragment mLocationlistFragment;
    //widget
    private ConstraintLayout clContainder;
    //var
    private List<LocationInfo> list;

    final String name[]={"a","b","c","d","e","f","g","h","i","j"};
    final GeoPoint geoPoint=new GeoPoint(55,63);

    @Rule
    public ActivityTestRule<MapsActivity> mapsActivityTestRule=new ActivityTestRule<MapsActivity>(MapsActivity.class);

    @Before
    public void setUp(){
        getLocationInfo();
        mMapActivity=mapsActivityTestRule.getActivity();
        clContainder=(ConstraintLayout) mMapActivity.findViewById(R.id.map_fragment_container);
        mLocationlistFragment=new LocationListFragment();
        mMapActivity.getSupportFragmentManager().beginTransaction().add(clContainder.getId(),mLocationlistFragment).commit();
        getInstrumentation().waitForIdleSync();
    }

    @Test
    public void testPreconditions()
    {
        Assert.assertNotNull(mMapActivity);
        Assert.assertNotNull(mLocationlistFragment);
        Assert.assertNotNull(clContainder);
    }

    @Test
    public void TestSort(){

        //mLocationlistFragment.sortItems(list,);
    }

    public void getLocationInfo(){
        for(int i=0;i<10;i++){
            List<String> upVote=getVote(i);
            List<String> downVote=new ArrayList<>();
            LocationInfo newLocation=new LocationInfo(geoPoint,name[i],upVote,downVote);
            list.add(newLocation);
        }
    }

    private List<String> getVote(int i){
        List<String> temp=new ArrayList<>();
        for(int j=0;j<j;j++){
            temp.add(name[i]);
        }
        return temp;
    }




}