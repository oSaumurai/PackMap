package edu.osu.cse5236.group10.packmap;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import edu.osu.cse5236.group10.packmap.data.model.LocationInfo;

public class LocationListFragment extends Fragment {
    private static final String TAG = "LocationListFragment";
    //var
    private String mUserId;
    private String mGroupId;
    private String mActivityId;
    private List<LocationInfo> locationInfoList;
    private List<String> locaitonUidList;
    private FirebaseFirestore db;
    //adapter
    private LocationListAdapter locationListAdapter;
    //recyclerview
    private RecyclerView mRecyclerView;

    public LocationListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUserId = getArguments().getString("userId");
            mGroupId = getArguments().getString("groupId");
            mActivityId=getArguments().getString("activityId");
            Log.d(TAG, "" + mUserId + " - " + mGroupId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_location_list, container, false);
        locationInfoList = new ArrayList<>();
        locaitonUidList=new ArrayList<>();
        locationListAdapter = new LocationListAdapter(mUserId, locationInfoList);

        mRecyclerView = v.findViewById(R.id.location_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(locationListAdapter);
        db = FirebaseFirestore.getInstance();

        db.collection("activities").document(mActivityId).addSnapshotListener((groupQuery, e1) -> {
            if (e1 != null) {
                Log.d(TAG, "error: " + e1.getMessage());
            } else {
                locaitonUidList.clear();
                locaitonUidList= (List<String>) groupQuery.get("selectedLocations");
                for (int i = 0; i < locaitonUidList.size(); i++) {
                    String locationUid=locaitonUidList.get(i);
                    db.collection("locations").document(locationUid).addSnapshotListener((locationQuery, e2)->{
                        if (e2!= null) {
                            Log.d(TAG, "error: " + e2.getMessage());
                        }else {
                            String name=(String) locationQuery.get("name");
                            int pos = -1;
                            for (int j=0;j<locationInfoList.size();j++)
                                if(name.equals(locationInfoList.get(j).getName()))
                                    pos = j;

                            List<String> upVote= (List<String>) locationQuery.get("upvotes");
                            List<String> downVote= (List<String>) locationQuery.get("downvotes");

                            if (pos == -1) {
                                // if pos == -1, the location is not in the list
                                GeoPoint geoPoint=locationQuery.getGeoPoint("coordinates");
                                LocationInfo locationInfo=new LocationInfo(geoPoint,name,upVote,downVote);
                                locationInfo.setUid(locationUid);
                                locationInfoList.add(locationInfo);
                                int currPos = locationInfoList.size() - 1, score = locationInfo.getIntScore();
                                while (currPos > 0 && score > locationInfoList.get(currPos - 1).getIntScore())
                                    currPos--;

                                Log.d(TAG, "Insert.");
                                Log.d(TAG, "score: " + score);
                                Log.d(TAG, "topos: " + currPos);

                                Collections.swap(locationInfoList, currPos, locationInfoList.size() - 1);
                                locationListAdapter.notifyDataSetChanged();
                            } else {
                                LocationInfo li = locationInfoList.get(pos);
                                li.setUpvotes(upVote);
                                li.setDownvotes(downVote);
                                int score = li.getIntScore(), currPos = pos, size = locationInfoList.size();

                                while (currPos > 0 && score > locationInfoList.get(currPos - 1).getIntScore())
                                    currPos--;
                                while (currPos < size - 1 && score < locationInfoList.get(currPos + 1).getIntScore())
                                    currPos++;

                                Log.d(TAG, "Swap.");
                                Log.d(TAG, "score: " + score);
                                Log.d(TAG, "from: " + pos);
                                Log.d(TAG, "to: " + currPos);

                                Collections.swap(locationInfoList, currPos, pos);
                                // locationListAdapter.notifyItemMoved(pos, currPos);
                                locationListAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                }

                Log.d(TAG, "listSize" + locationInfoList.size());
                for (int i = 0; i < locationInfoList.size(); ++i) {
                    Log.d(TAG, locationInfoList.get(i).getName());
                }
            }
        });

        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        Log.d(TAG, "onDetach() called");
    }








}
