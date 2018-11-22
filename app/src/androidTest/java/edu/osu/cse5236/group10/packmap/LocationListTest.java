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

import static org.junit.Assert.assertEquals;

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

    private LocationListFragment mLocationlistFragment;
    //widget
    private ConstraintLayout clContainder;
    //var
    private List<LocationInfo> list;

    final GeoPoint geoPoint=new GeoPoint(55,63);


    @Before
    public void setUp(){
        mLocationlistFragment=new LocationListFragment();
    }

    @Test
    public void testPreconditions()
    {
    }

    @Test
    public void TestVoteScore1(){
        LocationInfo li = new LocationInfo();

        li.setUpvotes(voteListHelper(4));
        li.setDownvotes(voteListHelper(0));

        assertEquals(li.getIntScore(), 4);
    }

    @Test
    public void TestVoteScore2(){
        LocationInfo li = new LocationInfo();

        li.setUpvotes(voteListHelper(4));
        li.setDownvotes(voteListHelper(1));

        assertEquals(li.getIntScore(), 3);
    }

    @Test
    public void TestVoteScore3(){
        LocationInfo li = new LocationInfo();

        li.setUpvotes(voteListHelper(0));
        li.setDownvotes(voteListHelper(1));

        assertEquals(li.getIntScore(), -1);
    }

    @Test
    public void TestVoteScore4(){
        LocationInfo li = new LocationInfo();

        li.setUpvotes(voteListHelper(2));
        li.setDownvotes(voteListHelper(3));

        assertEquals(li.getIntScore(), -1);
    }

    @Test
    public void TestVotePartialSort1(){
        List<LocationInfo> l = getSortedLocationInfo(5);
        l.get(1).setUpvotes(voteListHelper(0));

        assertEquals(l.get(1).getIntScore(), 0);

        mLocationlistFragment.sortItems(l, 1);

        assertEquals(l.get(0).getIntScore(), 5);
        assertEquals(l.get(1).getIntScore(), 3);
        assertEquals(l.get(2).getIntScore(), 2);
        assertEquals(l.get(3).getIntScore(), 1);
        assertEquals(l.get(4).getIntScore(), 0);
    }

    @Test
    public void TestVotePartialSort2(){
        List<LocationInfo> l = getSortedLocationInfo(5);
        l.get(1).setUpvotes(voteListHelper(7));

        assertEquals(l.get(1).getIntScore(), 7);

        mLocationlistFragment.sortItems(l, 1);

        assertEquals(l.get(0).getIntScore(), 7);
        assertEquals(l.get(1).getIntScore(), 5);
        assertEquals(l.get(2).getIntScore(), 3);
        assertEquals(l.get(3).getIntScore(), 2);
        assertEquals(l.get(4).getIntScore(), 1);
    }


    public List<LocationInfo> getSortedLocationInfo(int n){
        List<LocationInfo> ans = new ArrayList<>();

        for(int i=0;i<n;i++){
            List<String> upVote=voteListHelper(n - i);
            List<String> downVote=new ArrayList<>();
            LocationInfo newLocation=new LocationInfo(geoPoint, "dummy",upVote,downVote);
            ans.add(newLocation);
        }

        return ans;
    }

    private List<String> voteListHelper(int i){
        List<String> temp=new ArrayList<>();
        for(int j=0;j<i;j++){
            temp.add("dummy");
        }
        return temp;
    }




}