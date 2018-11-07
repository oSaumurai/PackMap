package edu.osu.cse5236.group10.packmap;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class PackMemberFragment extends Fragment {
    private static final String TAG = "PackMemberFragment";

    private RecyclerView mRecyclerView;
    private List<String> mUsers;
    private MemberListAdapter memberAdapter;
    private FirebaseFirestore db;

    private String mUserId;
    private String mGroupId;

    public PackMemberFragment() {
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
        View v = inflater.inflate(R.layout.fragment_pack_member, container, false);

        // RecyclerView configuration
        mUsers = new ArrayList<>();
        memberAdapter = new MemberListAdapter(mUsers);

        mRecyclerView = v.findViewById(R.id.pack_member_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(memberAdapter);

        db = FirebaseFirestore.getInstance();

        db.collection("groups").document(mGroupId).addSnapshotListener((querySnapshot, e) -> {
            if (e != null) {
                Log.d(TAG, "error: " + e.getMessage());
            } else {
                List<String> temp = (List<String>) querySnapshot.get("userList");
                mUsers.clear();
                for (String str: temp)
                    mUsers.add(str);

                memberAdapter.notifyDataSetChanged();

                Log.d(TAG, "" + mUsers.size());
            }
        });

        return v;
    }
}
