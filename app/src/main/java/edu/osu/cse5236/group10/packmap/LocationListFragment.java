package edu.osu.cse5236.group10.packmap;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.osu.cse5236.group10.packmap.data.model.ActivityInfo;
import edu.osu.cse5236.group10.packmap.data.model.LocationInfo;
import edu.osu.cse5236.group10.packmap.data.store.ActivityStore;

public class LocationListFragment extends Fragment {
    private static final String TAG = "LocationListFragment";
    //var
    private String mUserId;
    private String mGroupId;
    private String mActivityId;
    private List<LocationInfo> locationInfoList;
    private FirebaseFirestore db;
    //adapter
    private LocationListAdapter locationListAdapter;
    //listener
    private OnLocationListFragmentInteractionListener mListener;
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
        locationListAdapter = new LocationListAdapter(mActivityId, mUserId, locationInfoList, mListener);

        mRecyclerView = v.findViewById(R.id.location_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(locationListAdapter);
        db = FirebaseFirestore.getInstance();

        db.collection("activities").document(mActivityId).addSnapshotListener((groupQuery, e1) -> {
            if (e1 != null) {
                Log.d(TAG, "error: " + e1.getMessage());
            } else {
                List<String> locationUidList= (List<String>) groupQuery.get("selectedLocations");
                locationInfoList.clear();

                for (int i = 0; i < locationUidList.size(); i++) {
                    String locationUid=locationUidList.get(i);
                    db.collection("locations").document(locationUid).addSnapshotListener((locationQuery, e2)->{
                        if (e2!= null) {
                            Log.d(TAG, "error: " + e1.getMessage());
                        }else {
                            String name=(String) locationQuery.get("name");
                            GeoPoint geoPoint=locationQuery.getGeoPoint("coordinates");
                            List<String> upVote= new ArrayList<>();
                            upVote= (List<String>) locationQuery.get("upvotes");
                            List<String> downVote= new ArrayList<>();
                            downVote= (List<String>) locationQuery.get("downvotes");
                            LocationInfo locationInfo=new LocationInfo(geoPoint,name,upVote,downVote);
                            locationInfoList.add(locationInfo);
                            Log.d(TAG, "onCreateView: "+ locationInfoList.size());
                            locationListAdapter.notifyDataSetChanged();
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

        mListener = null;
    }


    public interface OnLocationListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(LocationInfo li);
    }






}
