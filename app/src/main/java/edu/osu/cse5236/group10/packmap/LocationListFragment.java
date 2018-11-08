package edu.osu.cse5236.group10.packmap;

import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.osu.cse5236.group10.packmap.data.model.ActivityInfo;
import edu.osu.cse5236.group10.packmap.data.store.ActivityStore;

public class LocationListFragment extends Fragment {
    private static final String TAG = "LocationListFragment";
    //var
    private String mUserId;
    private String mGroupId;
    private String mActivityId;
    private List<ActivityInfo> activityInfoList;
    private FirebaseFirestore db;
    //listener
    private PackFragment.OnPackFragmentInteractionListener mListener;
    //recyclerview
    private RecyclerView mRecyclerView;
    //botton
    private Button addActivityButton;

    public LocationListFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static PackFragment newInstance() {
        PackFragment fragment = new PackFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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
        mRecyclerView = v.findViewById(R.id.location_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        db = FirebaseFirestore.getInstance();

        db.collection("groups").document(mGroupId).addSnapshotListener((groupQuery, e1) -> {
            if (e1 != null) {
                Log.d(TAG, "error: " + e1.getMessage());
            } else {
                List<String> temp = (List<String>) groupQuery.get("activityList");
                Set<String> s = new HashSet<>(temp);
                activityInfoList.clear();

                db.collection("activities").addSnapshotListener((activityQuery, e2) -> {
                    if (e2 != null)
                        Log.d(TAG, "error" + e2.getMessage());
                    else {
                        for (DocumentSnapshot ds: activityQuery.getDocuments()) {
                            if (s.contains(ds.getId())) {
                                ActivityInfo temp1 = ds.toObject(ActivityInfo.class);
                                temp1.setUid(ds.getId());
                                activityInfoList.add(temp1);
                            }
                        }

                        //activityListAdapter.notifyDataSetChanged();
                        Log.d(TAG, "" + activityInfoList.size());
                        for (ActivityInfo ac: activityInfoList) {
                            Log.d(TAG, ac.getName());
                        }
                    }
                });
            }
        });

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

        Log.d(TAG, "onDetach() called");

        mListener = null;
    }


    public interface OnPackFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(ActivityInfo ai);
    }






}
