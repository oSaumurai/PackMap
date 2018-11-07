package edu.osu.cse5236.group10.packmap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import edu.osu.cse5236.group10.packmap.data.DataUtils;
import edu.osu.cse5236.group10.packmap.data.model.ActivityInfo;
import edu.osu.cse5236.group10.packmap.data.store.ActivityStore;

public class PackFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "PackFragment";
    //var
    private String mUserId;
    private String mGroupId;
    private ListView mlistView;
    private List<String> activityInfoList;
    private ActivityInfo newActivityInfo;
    private FirebaseFirestore db;
    //FireStore
    private ActivityStore mActivityStore;
    //listener
    private OnPackFragmentInteractionListener mListener;
    //adapter
    private ActivityListAdapter activityListAdapter;
    //recyclerview
    private RecyclerView mRecyclerView;
    //botton
    private Button addActivityButton;

    public PackFragment() {
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
            Log.d(TAG, "" + mUserId + " - " + mGroupId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_pack, container, false);
        activityInfoList=new ArrayList<>();
        activityListAdapter= new ActivityListAdapter(activityInfoList,mListener);

        newActivityInfo=new ActivityInfo();
        mActivityStore=ActivityStore.getInstance();
        mRecyclerView = v.findViewById(R.id.pack_activity_list);
        addActivityButton=v.findViewById(R.id.add_activity);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(activityListAdapter);
        db = FirebaseFirestore.getInstance();

        db.collection("groups").document(mGroupId).addSnapshotListener((querySnapshot, e) -> {
            if (e != null) {
                Log.d(TAG, "error: " + e.getMessage());
            } else {
                List<String> temp = (List<String>) querySnapshot.get("activityList");
                activityInfoList.clear();
                for (String str: temp)
                    activityInfoList.add(str);

                activityListAdapter.notifyDataSetChanged();
                Log.d(TAG, "" + activityInfoList.size());
            }
        });


        addActivityButton.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.add_activity:
                Log.d(TAG, "Add Activity");
                Intent intent = new Intent(getActivity(), CreateActivityActivity.class);
                intent.putExtra("groupId", mGroupId);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnPackFragmentInteractionListener) {
            mListener = (OnPackFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnPackFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        Log.d(TAG, "onDetach() called");

        mListener = null;
    }


    public interface OnPackFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(String name);
    }

}
